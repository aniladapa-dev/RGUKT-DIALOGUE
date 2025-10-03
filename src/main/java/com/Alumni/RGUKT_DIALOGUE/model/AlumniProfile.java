package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents Alumni-specific profile fields.
 * Extends base Profile.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AlumniProfiles")
@EqualsAndHashCode(callSuper = true)
public class AlumniProfile extends Profile {

    @Column(name = "student_id", length = 20, nullable = false, unique = true)
    private String studentId; // Unique alumni ID

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "personal_email", nullable = false, unique = true, length = 100)
    private String personalEmail;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    @JsonIgnore
    private String passwordHash; // Hide password hash in API

    @Column(name = "current_position", length = 100)
    private String currentPosition;

    @Column(name = "company", length = 100)
    private String company;

    // Back-reference to User
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Many-to-many relationship with Skills
    @ManyToMany
    @JoinTable(
            name = "alumni_skills",
            joinColumns = @JoinColumn(name = "alumni_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;

    // Many-to-many relationship with Certifications
    @ManyToMany
    @JoinTable(
            name = "alumni_certifications",
            joinColumns = @JoinColumn(name = "alumni_id"),
            inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    private Set<Certification> certifications;

    // Work experiences are fetched dynamically
    @Transient
    private Set<WorkExperience> experiences;
}
