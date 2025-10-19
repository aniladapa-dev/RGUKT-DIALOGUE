package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * DTO for adding a comment to a post.
 * Sent by the client as JSON in the request body.
 */
@Data
public class CommentRequest {

    /**
     * ID of the user who is commenting
     */
    private Long userId;

    /**
     * ID of the post to comment on
     */
    private Long postId;

    /**
     * The comment content/text
     */
    private String comment;
}
