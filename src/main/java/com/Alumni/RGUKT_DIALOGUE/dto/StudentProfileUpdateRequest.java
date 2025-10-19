package com.Alumni.RGUKT_DIALOGUE.dto;

import lombok.Data;
import java.util.Set;

@Data
public class StudentProfileUpdateRequest {

    private String bio;
    private String avatarUrl;
    private Integer enrollmentYear;
    private String department;
    private Integer currentSemester;
    private Double gpa;

    // Names of skills to add/update
    private Set<String> skills;

    // Names of certifications to add/update
    private Set<String> certifications;
}
