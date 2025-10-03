package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.AlumniProfile;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for AlumniProfile entity.
 * Provides CRUD operations + custom queries.
 */
public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {

    // Find alumni profile by studentId
    Optional<AlumniProfile> findByStudentId(String studentId);

    // Find alumni profile by associated User ID
    Optional<AlumniProfile> findByUserId(Long userId);

    // Find alumni profile by associated User object
    Optional<AlumniProfile> findByUser(User user);
}
