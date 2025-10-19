package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for User entity.
 * Provides CRUD operations, search, and connection-related queries.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (for login)
    Optional<User> findByEmail(String email);

    // Find users by exact name
    List<User> findByName(String name);

    // Search users by partial name (case-insensitive)
    List<User> findByNameContainingIgnoreCase(String name);

    // Fetch users whose IDs are NOT in excludedIds (for suggestions)
    Page<User> findByIdNotIn(List<Long> excludedIds, Pageable pageable);

    // Fallback: fetch all users with pagination
    Page<User> findAll(Pageable pageable);

    /**
     * Fetch all accepted connections for a user
     */
    @Query(
            value = "SELECT user2_id FROM connection WHERE user1_id = :userId AND status = 'ACCEPTED' " +
                    "UNION " +
                    "SELECT user1_id FROM connection WHERE user2_id = :userId AND status = 'ACCEPTED'",
            nativeQuery = true
    )
    List<Long> findAcceptedConnectionIds(@Param("userId") Long userId);



}
