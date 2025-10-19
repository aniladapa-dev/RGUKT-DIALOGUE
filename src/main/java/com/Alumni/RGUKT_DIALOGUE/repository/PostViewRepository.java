package com.Alumni.RGUKT_DIALOGUE.repository;

import com.Alumni.RGUKT_DIALOGUE.model.Post;
import com.Alumni.RGUKT_DIALOGUE.model.PostView;
import com.Alumni.RGUKT_DIALOGUE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, Long> {

    // Check if a user has already viewed a post
    boolean existsByUserAndPost(User user, Post post);
}
