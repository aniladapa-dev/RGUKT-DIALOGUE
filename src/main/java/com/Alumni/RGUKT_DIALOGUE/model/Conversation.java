package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Conversations", uniqueConstraints = @UniqueConstraint(columnNames = {"user1Id", "user2Id"}))
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1Id", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2Id", nullable = false)
    private User user2;

    private LocalDateTime lastMessageAt = LocalDateTime.now();
    private LocalDateTime createdAt = LocalDateTime.now();
}

