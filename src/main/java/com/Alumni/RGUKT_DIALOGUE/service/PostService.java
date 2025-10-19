package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.*;

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
}
