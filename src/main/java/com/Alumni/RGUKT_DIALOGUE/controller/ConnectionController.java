package com.Alumni.RGUKT_DIALOGUE.controller;

import com.Alumni.RGUKT_DIALOGUE.dto.*;
import com.Alumni.RGUKT_DIALOGUE.model.ConnectionRequest;
import com.Alumni.RGUKT_DIALOGUE.service.ConnectionService;
import com.Alumni.RGUKT_DIALOGUE.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Endpoints:
 * POST   /api/connections/requests      -> send a request
 * GET    /api/connections/requests/incoming -> list incoming pending requests
 * POST   /api/connections/requests/{id}/respond -> accept/reject
 * GET    /api/connections              -> list connections
 * GET    /api/connections/suggestions?limit=10 -> suggestions
 *
 * This controller expects Authorization header "Bearer <token>" and uses JwtService to extract user id.
 */
@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;
    private final JwtService jwtService;

    // Send a connection request
    @PostMapping("/requests")
    public ResponseEntity<?> sendRequest(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody SendConnectionRequestDto dto) {

        Long senderId = jwtService.extractUserId(authHeader.substring(7));
        ConnectionRequest cr = connectionService.sendRequest(senderId, dto.getReceiverId(), dto.getMessage());

        return ResponseEntity.status(201).body(
                new ConnectionRequestDto(
                        cr.getId(),
                        cr.getSender().getId(),
                        cr.getSender().getName(),
                        cr.getSender().getEmail(),
                        cr.getMessage(),
                        cr.getStatus().name(),
                        cr.getCreatedAt()
                )
        );
    }

    // List incoming pending requests
    @GetMapping("/requests/incoming")
    public ResponseEntity<List<ConnectionRequestDto>> incoming(@RequestHeader("Authorization") String authHeader) {
        Long userId = jwtService.extractUserId(authHeader.substring(7));
        List<ConnectionRequest> pendings = connectionService.getIncomingPendingRequests(userId);

        List<ConnectionRequestDto> dtos = pendings.stream().map(cr -> new ConnectionRequestDto(
                cr.getId(),
                cr.getSender().getId(),
                cr.getSender().getName(),
                cr.getSender().getEmail(),
                cr.getMessage(),
                cr.getStatus().name(),
                cr.getCreatedAt()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // Accept / reject request
    @PostMapping("/requests/{id}/respond")
    public ResponseEntity<?> respond(@RequestHeader("Authorization") String authHeader,
                                     @PathVariable Long id,
                                     @RequestBody RespondConnectionRequestDto dto) {
        Long userId = jwtService.extractUserId(authHeader.substring(7));
        connectionService.respondToRequest(userId, id, dto.getAccept());
        return ResponseEntity.ok().body(Map.of("success", true));
    }

    // List connections
    @GetMapping
    public ResponseEntity<List<ConnectionDto>> connections(@RequestHeader("Authorization") String authHeader) {
        Long userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(connectionService.getConnections(userId));
    }

    // Suggestions
    @GetMapping("/suggestions")
    public ResponseEntity<List<ConnectionDto>> suggestions(@RequestHeader("Authorization") String authHeader,
                                                           @RequestParam(defaultValue = "10") int limit) {
        Long userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(connectionService.suggestConnections(userId, limit));
    }
}
