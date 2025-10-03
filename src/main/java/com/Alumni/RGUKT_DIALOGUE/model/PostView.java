package com.Alumni.RGUKT_DIALOGUE.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PostViews", uniqueConstraints = @UniqueConstraint(columnNames = {"postId", "userId"}))
public class PostView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private LocalDateTime viewedAt = LocalDateTime.now();
}

