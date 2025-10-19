package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Like;
import com.Alumni.RGUKT_DIALOGUE.model.Post;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Like entity
 */
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);

    // Count likes for a post without fetching entire list
    long countByPost(Post post);

    boolean existsByPostAndUser(Post post, User user);
}
