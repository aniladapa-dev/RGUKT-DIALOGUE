package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String branch;
    private String email;
    private String password;

    private boolean active = true; // block/unblock admin
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

/*
{
  "email": "ravi.kumar@rgukt.ac.in",
  "password": "admin123"
}

 */
