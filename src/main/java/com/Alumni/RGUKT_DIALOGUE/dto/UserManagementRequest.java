package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

@Data
public class UserManagementRequest {
    private String name;
    private String email;
    private String password;
    private String role; // STUDENT / ALUMNI
}
