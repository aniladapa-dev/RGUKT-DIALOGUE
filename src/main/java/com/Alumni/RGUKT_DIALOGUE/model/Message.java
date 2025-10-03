package com.Alumni.RGUKT_DIALOGUE.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversationId", nullable = false)
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false)
    private User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private boolean read = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}
