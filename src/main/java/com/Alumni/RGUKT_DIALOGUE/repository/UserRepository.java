package com.Alumni.RGUKT_DIALOGUE.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
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

    /**
     * Find users whose id is NOT in the excluded list (used for suggestions).
     */
    Page<User> findByIdNotIn(List<Long> excludedIds, Pageable pageable);

    /**
     * If excluded list is empty, fallback to findAll with paging.
     */
    Page<User> findAll(Pageable pageable);
}
