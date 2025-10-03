package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "WorkExperiences")
public class WorkExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to AlumniProfile entity
    @ManyToOne
    @JoinColumn(name = "alumni_profile_id", nullable = false)
    private AlumniProfile alumniProfile;

    private String companyName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate; // null if currently working
    private String description; // optional
}
