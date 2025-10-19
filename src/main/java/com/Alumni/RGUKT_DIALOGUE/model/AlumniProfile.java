package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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
@EqualsAndHashCode(callSuper = true, exclude = {"skills", "certifications"})
public class AlumniProfile extends Profile {

    @Column(name = "student_id", length = 20, nullable = false, unique = true)
    private String studentId;

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
    private String passwordHash;

    @Column(name = "current_position", length = 100)
    private String currentPosition;

    @Column(name = "company", length = 100)
    private String company;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "alumni_skills",  // or alumni_skills table if separate
            joinColumns = @JoinColumn(name = "alumni_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonManagedReference
    private Set<Skill> skills = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "alumni_certifications", // or alumni_certifications table
            joinColumns = @JoinColumn(name = "alumni_id"),
            inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    @JsonManagedReference
    private Set<Certification> certifications = new HashSet<>();

    // Work experiences dynamically loaded
    @Transient
    private Set<WorkExperience> experiences;
}
