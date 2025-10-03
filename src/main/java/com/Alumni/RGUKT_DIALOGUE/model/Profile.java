package com.Alumni.RGUKT_DIALOGUE.model;

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

    @OneToOne  // One-to-one relationship with User
    @JoinColumn(name = "userId", nullable = false, unique = true)
    // 'userId' will be foreign key pointing to 'id' in Users table
    private User user;  // Profile belongs to one user

    private String bio;        // Short biography of the user
    private String avatarUrl;  // Profile picture URL
}
