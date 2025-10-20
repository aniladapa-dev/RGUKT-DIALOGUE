package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

@Data
public class AdminRegisterUserRequest {
    private String name;
    private String email;
    private String branch;
    private String password;


}
