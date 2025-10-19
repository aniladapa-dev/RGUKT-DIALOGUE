package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Connection;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    /**
     * Find all connection records that include the given user (either user1 or user2).
     */
    @Query("SELECT c FROM Connection c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<Connection> findByUserId(@Param("userId") Long userId);

    /**
     * Check if a connection exists between two users (unordered pair).
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Connection c " +
            "WHERE (c.user1.id = :a AND c.user2.id = :b) OR (c.user1.id = :b AND c.user2.id = :a)")
    boolean existsBetween(@Param("a") Long a, @Param("b") Long b);

    /**
     * Get the IDs of users who are connected to the given user.
     */
    @Query("SELECT CASE WHEN c.user1.id = :userId THEN c.user2.id ELSE c.user1.id END " +
            "FROM Connection c WHERE c.user1.id = :userId OR c.user2.id = :userId")
    List<Long> findConnectedUserIds(@Param("userId") Long userId);

    /**
     * Get the list of User entities connected (accepted) with a given user.
     */
    @Query(
            value = "SELECT user2_id FROM connection WHERE user1_id = :userId AND status = 'ACCEPTED' " +
                    "UNION " +
                    "SELECT user1_id FROM connection WHERE user2_id = :userId AND status = 'ACCEPTED'",
            nativeQuery = true
    )
    List<Long> findAcceptedConnectionIds(@Param("userId") Long userId);




}
