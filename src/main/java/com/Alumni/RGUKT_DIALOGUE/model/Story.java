package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
}


