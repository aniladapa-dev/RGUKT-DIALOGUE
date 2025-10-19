package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * DTO for liking a post.
 * Sent by the client as JSON in the request body.
 */
@Data
public class LikeRequest {

    /**
     * ID of the user who is liking the post
     */
    private Long userId;

    /**
     * ID of the post to be liked
     */
    private Long postId;
}
