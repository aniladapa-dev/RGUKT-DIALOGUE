package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.AlumniProfile;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for AlumniProfile entity.
 * Provides CRUD operations + custom queries.
 */
public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {

    /**
     * Fetch alumni profile by student ID.
     */
    Optional<AlumniProfile> findByStudentId(String studentId);

    /**
     * Fetch alumni profile by user ID with eager fetching of
     * 'skills' and 'certifications' to avoid empty arrays in JSON.
     */
    @EntityGraph(attributePaths = {"skills", "certifications"})
    Optional<AlumniProfile> findByUserId(Long userId);

    /**
     * Fetch alumni profile by User object.
     */
    Optional<AlumniProfile> findByUser(User user);
}
