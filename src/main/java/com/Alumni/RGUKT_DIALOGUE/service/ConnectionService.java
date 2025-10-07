package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.ConnectionDto;
import com.Alumni.RGUKT_DIALOGUE.model.Connection;
import com.Alumni.RGUKT_DIALOGUE.model.ConnectionRequest;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import com.Alumni.RGUKT_DIALOGUE.repository.ConnectionRepository;
import com.Alumni.RGUKT_DIALOGUE.repository.ConnectionRequestRepository;
import com.Alumni.RGUKT_DIALOGUE.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ConnectionService handles:
 *  - Sending connection requests
 *  - Accepting / rejecting requests (atomic creation of Connection)
 *  - Fetching incoming requests
 *  - Fetching connections list
 *  - Suggesting users to connect with
 *
 * Important invariants:
 * - For Connection entity, we store user1.id < user2.id (canonical ordering).
 * - Unique constraints on (user1Id, user2Id) and on (senderId, receiverId) in ConnectionRequest entity.
 */
@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRequestRepository requestRepo;
    private final ConnectionRepository connectionRepo;
    private final UserRepository userRepo;

    /**
     * Send a connection request from senderId to receiverId.
     * - prevent self-request
     * - prevent duplicate pending/accepted requests
     * - if a reverse PENDING request exists (receiver previously sent to sender), auto-accept it and create Connection
     */
    @Transactional
    public ConnectionRequest sendRequest(Long senderId, Long receiverId, String message) {
        if (Objects.equals(senderId, receiverId)) {
            throw new IllegalArgumentException("Cannot send connection request to yourself");
        }

        User sender = userRepo.findById(senderId).orElseThrow(() -> new NoSuchElementException("Sender not found"));
        User receiver = userRepo.findById(receiverId).orElseThrow(() -> new NoSuchElementException("Receiver not found"));

        // If already connected
        if (connectionRepo.existsBetween(senderId, receiverId)) {
            throw new IllegalStateException("Already connected with this user");
        }

        // If already a pending request from same sender -> receiver
        Optional<ConnectionRequest> existing = requestRepo.findBySenderIdAndReceiverId(senderId, receiverId);
        if (existing.isPresent()) {
            ConnectionRequest cr = existing.get();
            if (cr.getStatus() == ConnectionRequest.Status.PENDING) {
                throw new IllegalStateException("Connection request already pending");
            } else if (cr.getStatus() == ConnectionRequest.Status.ACCEPTED) {
                throw new IllegalStateException("Already connected (request accepted)");
            } else {
                // previously rejected - allow re-send by updating status to PENDING
                cr.setStatus(ConnectionRequest.Status.PENDING);
                cr.setMessage(message);
                cr.setCreatedAt(LocalDateTime.now());
                return requestRepo.save(cr);
            }
        }

        // Check reverse pending: receiver previously sent to sender
        Optional<ConnectionRequest> reversePending = requestRepo.findPendingBySenderAndReceiver(receiverId, senderId);
        if (reversePending.isPresent()) {
            // Accept the pending reverse request and create a Connection
            ConnectionRequest reverse = reversePending.get();
            reverse.setStatus(ConnectionRequest.Status.ACCEPTED);
            requestRepo.save(reverse);

            createConnectionRecord(senderId, receiverId);
            // Return the reverse request (now accepted)
            return reverse;
        }

        // No existing request — create new pending request
        ConnectionRequest request = new ConnectionRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setMessage(message);
        request.setStatus(ConnectionRequest.Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return requestRepo.save(request);
    }

    /**
     * Accept or reject a pending connection request. Only the receiver can perform this.
     */
    @Transactional
    public void respondToRequest(Long authUserId, Long requestId, boolean accept) {
        ConnectionRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));

        if (!Objects.equals(req.getReceiver().getId(), authUserId)) {
            throw new SecurityException("Only the receiver can accept or reject this request");
        }

        if (req.getStatus() != ConnectionRequest.Status.PENDING) {
            throw new IllegalStateException("Request already handled");
        }

        if (accept) {
            req.setStatus(ConnectionRequest.Status.ACCEPTED);
            requestRepo.save(req);

            createConnectionRecord(req.getSender().getId(), req.getReceiver().getId());
        } else {
            req.setStatus(ConnectionRequest.Status.REJECTED);
            requestRepo.save(req);
        }
    }

    /**
     * Create a Connection entity (canonical ordering: user1.id < user2.id).
     * If connection already created by concurrent transaction, swallow unique violation by checking existsBetween first.
     */
    private void createConnectionRecord(Long aId, Long bId) {
        long u1 = Math.min(aId, bId);
        long u2 = Math.max(aId, bId);

        if (connectionRepo.existsBetween(u1, u2)) {
            return; // already created by concurrent process
        }

        User user1 = userRepo.findById(u1).orElseThrow(() -> new NoSuchElementException("User not found"));
        User user2 = userRepo.findById(u2).orElseThrow(() -> new NoSuchElementException("User not found"));

        Connection connection = new Connection();
        connection.setUser1(user1);
        connection.setUser2(user2);
        connection.setConnectedAt(LocalDateTime.now());
        connectionRepo.save(connection);
    }

    /**
     * List incoming pending requests for a user.
     */
    public List<ConnectionRequest> getIncomingPendingRequests(Long userId) {
        return requestRepo.findByReceiverIdAndStatus(userId, ConnectionRequest.Status.PENDING);
    }

    /**
     * Get a list of connection DTOs (profiles) for a user.
     */
    public List<ConnectionDto> getConnections(Long userId) {
        List<Long> connectedIds = connectionRepo.findConnectedUserIds(userId);
        if (connectedIds.isEmpty()) return Collections.emptyList();

        List<User> users = userRepo.findAllById(connectedIds);
        return users.stream()
                .map(u -> new ConnectionDto(u.getId(), u.getName(), u.getEmail(), u.getRole() == null ? "" : u.getRole().name()))
                .collect(Collectors.toList());
    }

    /**
     * Suggest users to connect with:
     * - exclude self
     * - exclude already connected users
     * - exclude pending senders/receivers
     * - order by simple heuristic: mutual connections count (descending)
     *
     * This is a naive implementation and can be optimized later with SQL / pre-computed values.
     */
    public List<ConnectionDto> suggestConnections(Long userId, int limit) {
        // 1) excluded: self + connected users
        Set<Long> excluded = new HashSet<>();
        excluded.add(userId);
        excluded.addAll(connectionRepo.findConnectedUserIds(userId));

        // 2) exclude users with pending incoming or outgoing requests
        List<ConnectionRequest> incomingPending = requestRepo.findByReceiverIdAndStatus(userId, ConnectionRequest.Status.PENDING);
        for (ConnectionRequest cr : incomingPending) excluded.add(cr.getSender().getId());
        List<ConnectionRequest> outgoingPending = requestRepo.findBySenderIdAndStatus(userId, ConnectionRequest.Status.PENDING);
        for (ConnectionRequest cr : outgoingPending) excluded.add(cr.getReceiver().getId());

        // 3) fetch candidates (paged)
        Pageable p = PageRequest.of(0, limit);
        List<User> candidates;
        if (excluded.isEmpty()) {
            candidates = userRepo.findAll(p).getContent();
        } else {
            candidates = userRepo.findByIdNotIn(new ArrayList<>(excluded), p).getContent();
        }

        // 4) compute simple mutual connection counts and sort
        List<Long> myConnections = connectionRepo.findConnectedUserIds(userId);
        Set<Long> myConnSet = new HashSet<>(myConnections);

        List<ConnectionDto> dtoList = candidates.stream().map(u -> {
                    // compute mutual: number of u's connections intersect myConnSet
                    List<Long> otherConns = connectionRepo.findConnectedUserIds(u.getId());
                    long mutual = 0;
                    for (Long id : otherConns) if (myConnSet.contains(id)) mutual++;
                    ConnectionDto dto = new ConnectionDto(u.getId(), u.getName(), u.getEmail(), u.getRole() == null ? "" : u.getRole().name());
                    // we will sort later using mutual count (pack as map)
                    return Map.entry(dto, mutual);
                }).sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // trim to requested limit (safety)
        if (dtoList.size() > limit) return dtoList.subList(0, limit);
        return dtoList;
    }
}
