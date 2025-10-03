package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  //A user can create multiple posts.
    //But each post belongs to one user only.
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    //@Column
    //This is telling JPA how to map this field to a database column.
    private String content;

    private String mediaUrl;
    private int likesCount = 0;
    private int viewsCount = 0;

    private LocalDateTime createdAt = LocalDateTime.now();
}

