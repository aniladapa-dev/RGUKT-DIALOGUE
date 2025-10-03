package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.StudentProfile;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for StudentProfile entity.
 * Provides CRUD operations + custom queries.
 */
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    // Find student profile by associated User ID
    Optional<StudentProfile> findByUserId(Long userId);

    // Find student profile by associated User object
    Optional<StudentProfile> findByUser(User user);
}
