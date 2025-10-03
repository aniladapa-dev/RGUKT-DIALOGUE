package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Represents Student-specific profile fields.
 * Extends base Profile.
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "StudentProfiles")
@EqualsAndHashCode(callSuper = true)
public class StudentProfile extends Profile {

    private Integer enrollmentYear; // Year of admission
    private String department;      // Department name
    private Integer currentSemester; // Current semester
    private Double gpa;             // Grade Point Average

    // Back-reference to User to avoid infinite JSON recursion
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Many-to-many relationship with Skills
    @ManyToMany
    @JoinTable(
            name = "student_skills",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills;

    // Many-to-many relationship with Certifications
    @ManyToMany
    @JoinTable(
            name = "student_certifications",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    private Set<Certification> certifications;
}
