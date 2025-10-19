package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;
import java.util.Set;

@Data
public class AlumniProfileUpdateRequest {

    private String bio;
    private String avatarUrl;

    private String studentId;
    private String name;
    private String personalEmail;
    private String mobileNumber;
    private String username;
    private String currentPosition;
    private String company;

    // Names of skills to add/update
    private Set<String> skills;

    // Names of certifications to add/update
    private Set<String> certifications;
}
