package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for Post entity.
 * Provides custom queries for dashboard feed.
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /** Fetch all posts created by a specific user */
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Fetch posts from multiple users (used for feed)
     * Ordered by latest first
     */
    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    List<Post> findByUserIds(@Param("userIds") List<Long> userIds);
}
