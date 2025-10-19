package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * DTO for sending like details in API responses.
 */
@Data
public class LikeResponse {

    /**
     * ID of the user who liked the post
     */
    private Long userId;

    /**
     * ID of the post liked
     */
    private Long postId;
}
