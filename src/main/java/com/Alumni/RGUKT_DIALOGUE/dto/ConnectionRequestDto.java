package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ConnectionRequestDto {
    private Long requestId;
    private Long senderId;
    private String senderName;
    private String senderEmail;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}
