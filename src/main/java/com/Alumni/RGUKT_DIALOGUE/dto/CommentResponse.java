package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for sending comment details in API responses.
 */
@Data
public class CommentResponse {

    /**
     * Unique ID of the comment
     */
    private Long commentId;

    /**
     * ID of the user who commented
     */
    private Long userId;

    /**
     * The comment text/content
     */
    private String comment;

    /**
     * Timestamp when the comment was created
     */
    private LocalDateTime createdAt;
}
