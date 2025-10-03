package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ConnectionRequests", uniqueConstraints = @UniqueConstraint(columnNames = {"senderId", "receiverId"}))
public class ConnectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@ManyToOne → Many messages can be sent by the same user.
    //Each message belongs to exactly one sender.
    //@JoinColumn(name = "senderId") → In the Message table, there will be a column senderId which is a foreign key to Users.id.
    //nullable = false → Every message must have a sender.
    @JoinColumn(name = "senderId", nullable = false)
    private User sender;

    @ManyToOne
    //Same logic as above, but for the receiver.
    //Many messages can be received by the same user.
    //Each message has one receiver.
    //Column receiverId → foreign key to Users.id.
    @JoinColumn(name = "receiverId", nullable = false)
    private User receiver;

    private String message;

    @Enumerated(EnumType.STRING)
    //This is an enum field (Status is an enum you define).
    //@Enumerated(EnumType.STRING) → tells JPA to store the enum as a string (PENDING, DELIVERED, READ) instead of a number.
    //Default value is PENDING.
    private Status status = Status.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        PENDING, ACCEPTED, REJECTED
    }
}
