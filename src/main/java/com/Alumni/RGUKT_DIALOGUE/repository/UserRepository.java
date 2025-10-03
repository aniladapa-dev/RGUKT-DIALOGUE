package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for User entity.
 * Provides standard CRUD operations + custom queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (unique)
    Optional<User> findByEmail(String email);

    // Find user by name (optional, for search purposes)
    Optional<User> findByName(String name);
}
