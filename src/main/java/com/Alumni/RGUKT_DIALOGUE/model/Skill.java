package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "skills")
    @JsonBackReference
    private Set<StudentProfile> students = new HashSet<>();

    @ManyToMany(mappedBy = "skills")
    @JsonBackReference
    private Set<AlumniProfile> alumni = new HashSet<>();


    /**
     * Constructor to create a skill with name
     * Ensures lowercase and initializes collections
     */
    public Skill(String name) {
        this.name = name.toLowerCase();
        this.students = new HashSet<>();
        this.alumni = new HashSet<>();
    }
}
