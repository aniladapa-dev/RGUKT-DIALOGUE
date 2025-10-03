package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for login response.
 * Contains JWT token and user ID.
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
}
