package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.*;
import com.Alumni.RGUKT_DIALOGUE.model.Post;

import java.util.List;

/**
 * Service interface for managing posts using DTOs
 */
public interface PostService {

    PostResponse createPost(PostRequest postRequest);

    List<PostResponse> getUserPosts(Long userId);

    List<PostResponse> getFeed(Long userId);

    List<PostResponse> getAllPosts();

    PostResponse likePost(LikeRequest likeRequest);

    PostResponse commentOnPost(CommentRequest commentRequest);

    // --- Admin operations ---
    Post getPostById(Long postId);

    void savePost(Post post);

    void deletePost(Long postId);
}
