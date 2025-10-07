package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple DTO to return connection profile info.
 */
@Data
@AllArgsConstructor
public class ConnectionDto {
    private Long id;
    private String name;
    private String email;
    private String role;
}
