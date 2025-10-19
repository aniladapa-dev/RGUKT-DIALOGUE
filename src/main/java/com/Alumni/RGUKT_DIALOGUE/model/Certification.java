package com.Alumni.RGUKT_DIALOGUE.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "Certifications")
@AllArgsConstructor
@NoArgsConstructor
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "AWS Certified"

    // Students with this certification
    @ManyToMany(mappedBy = "certifications")
    @JsonBackReference
    private Set<StudentProfile> students;

    // Alumni with this certification
    @ManyToMany(mappedBy = "certifications")
    @JsonBackReference
    private Set<AlumniProfile> alumni;
}
