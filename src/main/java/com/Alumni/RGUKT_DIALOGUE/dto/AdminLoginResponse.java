package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {
    private String token;
    private String name;
    private String branch;
}
