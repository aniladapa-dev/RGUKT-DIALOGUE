package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "StoryViews", uniqueConstraints = @UniqueConstraint(columnNames = {"storyId", "viewerId"}))
public class StoryView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "storyId", nullable = false)
    private Story story;

    @ManyToOne
    @JoinColumn(name = "viewerId", nullable = false)
    private User viewer;

    private LocalDateTime viewedAt = LocalDateTime.now();
}

