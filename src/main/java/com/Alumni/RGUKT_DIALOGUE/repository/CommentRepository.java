package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Comment;
import com.Alumni.RGUKT_DIALOGUE.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Comment entity
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Fetch all comments of a post (optional: for admin/debug)
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);

    // Count comments without fetching entire list
    long countByPost(Post post);
}
