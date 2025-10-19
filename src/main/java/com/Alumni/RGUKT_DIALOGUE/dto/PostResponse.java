package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for sending post details in API responses.
 */
@Data
public class PostResponse {

    /**
     * Unique ID of the post
     */
    private Long postId;

    /**
     * ID of the user who created the post
     */
    private Long userId;

    /**
     * Content/text of the post
     */
    private String content;

    /**
     * Optional image URL
     */
    private String imageUrl;

    /**
     * Total number of likes
     */
    private int likeCount;

    /**
     * Total number of comments
     */
    private int commentCount;

    /**
     * Post creation timestamp
     */
    private LocalDateTime createdAt;
}
