package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity representing user-generated posts on the platform.
 * Each post belongs to one user.
 */
@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many posts can belong to one user.
     * CascadeType.ALL is NOT used here to avoid accidental user deletion.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Text content of the post */
    @Column(columnDefinition = "TEXT")
    private String content;

    /** Optional image URL if post contains an image */
    private String imageUrl;

    /** Timestamp when post was created */
    private LocalDateTime createdAt = LocalDateTime.now();

}
