package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * Payload to accept or reject a connection request.
 */
@Data
public class RespondConnectionRequestDto {
    private Boolean accept; // true => accept, false => reject
}
