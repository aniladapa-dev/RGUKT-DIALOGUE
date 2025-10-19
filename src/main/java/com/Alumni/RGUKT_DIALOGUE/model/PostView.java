package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post_views", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
public class PostView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Post viewed
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // User who viewed
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Timestamp
    private LocalDateTime viewedAt = LocalDateTime.now();
}
