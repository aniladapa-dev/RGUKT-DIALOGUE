package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.WorkExperience;
import com.Alumni.RGUKT_DIALOGUE.model.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for WorkExperience entity.
 * Stores work experiences related to alumni.
 */
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {

    // Fetch all experiences of a specific alumni
    List<WorkExperience> findByAlumniProfile(AlumniProfile alumniProfile);
}
