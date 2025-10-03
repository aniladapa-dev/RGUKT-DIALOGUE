package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Certification entity.
 * Used to store and fetch certifications.
 */
public interface CertificationRepository extends JpaRepository<Certification, Long> {

    // Find certification by name
    Optional<Certification> findByName(String name);
}
