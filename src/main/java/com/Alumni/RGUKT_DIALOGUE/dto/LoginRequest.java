package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

/**
 * DTO for login request.
 * Only email and password are required.
 */
@Data
public class LoginRequest {
    private String email;
    private String password;
}
