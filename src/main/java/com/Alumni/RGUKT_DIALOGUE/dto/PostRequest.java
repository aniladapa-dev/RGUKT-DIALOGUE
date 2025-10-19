package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * DTO for creating a new post.
 * Sent by the client as JSON in the request body.
 */
@Data
public class PostRequest {

    /**
     * ID of the user creating the post
     */
    private Long userId;

    /**
     * Content/text of the post
     */
    private String content;

    /**
     * Optional image URL for the post
     */
    private String imageUrl;
}
