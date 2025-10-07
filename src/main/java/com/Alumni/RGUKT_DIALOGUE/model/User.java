package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Users") // Table name in DB
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key

    private String name; // Full name of user

    @Column(unique = true, nullable = false)
    private String email; // Unique email, required

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @Enumerated(EnumType.STRING) // Store enum as string in DB
    private Role role; // STUDENT, ALUMNI, ADMIN

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Enum for user roles
    public enum Role {
        STUDENT, ALUMNI, ADMIN
    }

    // One-to-one relationship with StudentProfile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference // Avoid infinite JSON recursion
    private StudentProfile studentProfile;

    // One-to-one relationship with AlumniProfile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private AlumniProfile alumniProfile;
}

/*
User → holds account info.

Profile → holds extended user info.

Login → verifies user + generates JWT.

JwtService → creates, parses, validates JWT.

JwtFilter → checks JWT on every request.

MyUserDetailService → converts User to Spring Security format.

SecurityContextHolder → stores authentication info.

Protected APIs → only accessible if JWT is valid.
 */

/*
{
  "name": "mona",
  "email": "mona@gamil.com",
  "password": "123456",
  "role": "STUDENT"
}

{
  "name": "sona",
  "email": "sona@gamil.com",
  "password": "123456",
  "role": "STUDENT"
}



 */
