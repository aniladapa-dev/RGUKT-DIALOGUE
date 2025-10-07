package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.StudentProfile;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for StudentProfile entity.
 * Provides CRUD operations + custom queries.
 */
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    /**
     * Fetch a student profile by user ID.
     *
     * Using @EntityGraph with attributePaths ensures that
     * the associated 'skills' and 'certifications' Sets
     * are eagerly loaded along with the profile.
     * This avoids empty arrays when converting to JSON.
     */
    @EntityGraph(attributePaths = {"skills", "certifications"})
    Optional<StudentProfile> findByUserId(Long userId);

    /**
     * Fetch a student profile by User object.
     * No eager fetching here, just a simple optional find.
     */
    Optional<StudentProfile> findByUser(User user);
}
