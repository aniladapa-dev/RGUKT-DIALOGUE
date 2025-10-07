package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.ConnectionRequest;
import com.Alumni.RGUKT_DIALOGUE.model.ConnectionRequest.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {

    Optional<ConnectionRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<ConnectionRequest> findByReceiverIdAndStatus(Long receiverId, Status status);

    List<ConnectionRequest> findBySenderIdAndStatus(Long senderId, Status status);

    @Query("SELECT CASE WHEN COUNT(cr) > 0 THEN true ELSE false END FROM ConnectionRequest cr " +
            "WHERE cr.sender.id = :sender AND cr.receiver.id = :receiver AND cr.status = :status")
    boolean existsBySenderReceiverAndStatus(@Param("sender") Long sender,
                                            @Param("receiver") Long receiver,
                                            @Param("status") Status status);

    /**
     * Find a pending request in the reverse direction (receiver had earlier sent to sender)
     */
    @Query("SELECT cr FROM ConnectionRequest cr WHERE cr.sender.id = :sender AND cr.receiver.id = :receiver AND cr.status = 'PENDING'")
    Optional<ConnectionRequest> findPendingBySenderAndReceiver(@Param("sender") Long sender, @Param("receiver") Long receiver);
}
