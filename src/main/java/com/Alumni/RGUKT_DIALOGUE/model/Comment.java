package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who commented
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // The post on which comment is made
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // The actual comment content
    private String comment;  // MUST match 'setComment()'

    // Timestamp
    private LocalDateTime createdAt;  // MUST match 'setCreatedAt()'
}
