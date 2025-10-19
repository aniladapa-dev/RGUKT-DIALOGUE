package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Base fields shared by StudentProfile and AlumniProfile.
 * This is NOT an entity/table itself.
 * Any class extending Profile will inherit these fields.
 */

@Data
@MappedSuperclass  // Not a table itself, just shared fields
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key for all profiles

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String bio;        // Short biography of the user
    private String avatarUrl;  // Profile picture URL
}
