package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminRegisterUsersRequest {
    private List<AdminRegisterUserRequest> users;
}
