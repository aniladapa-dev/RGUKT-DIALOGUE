package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlumniAccount extends JpaRepository<AlumniProfile, String> {

    Optional<AlumniProfile> findByUsername(String username);

    Optional<AlumniProfile> findByPersonalEmail(String personalEmail);
}

