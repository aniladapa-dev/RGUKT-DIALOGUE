package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Skill entity.
 * Used to store and fetch skills.
 */
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // Find skill by name
    Optional<Skill> findByName(String name);
}
