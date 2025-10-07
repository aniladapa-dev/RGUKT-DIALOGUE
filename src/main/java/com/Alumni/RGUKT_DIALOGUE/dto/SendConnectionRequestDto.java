package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * Payload to send a connection request.
 */
@Data
public class SendConnectionRequestDto {
    private Long receiverId;
    private String message; // optional personal message
}
