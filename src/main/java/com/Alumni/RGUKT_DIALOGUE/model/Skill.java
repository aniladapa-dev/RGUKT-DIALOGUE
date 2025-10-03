package com.Alumni.RGUKT_DIALOGUE.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "Skills")
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Java"

    // Students with this skill
    @ManyToMany(mappedBy = "skills")
    private Set<StudentProfile> students;

    // Alumni with this skill
    @ManyToMany(mappedBy = "skills")
    private Set<AlumniProfile> alumni;
}
