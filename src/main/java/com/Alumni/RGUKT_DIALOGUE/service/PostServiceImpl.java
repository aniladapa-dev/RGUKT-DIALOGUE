package com.Alumni.RGUKT_DIALOGUE.service;

import com.Alumni.RGUKT_DIALOGUE.dto.*;
import com.Alumni.RGUKT_DIALOGUE.model.*;
import com.Alumni.RGUKT_DIALOGUE.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Implementation of PostService using DTOs and optimized counts.
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ConnectionRepository connectionRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    //  Create a post
    @Override
    public PostResponse createPost(PostRequest postRequest) {
        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(postRequest.getContent());
        post.setImageUrl(postRequest.getImageUrl());
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);
        return mapToPostResponse(post);
    }

    // ---------------- Get posts of a user ----------------
    @Override
    public List<PostResponse> getUserPosts(Long userId) {
        List<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return posts.stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    // ---------------- Get personalized feed ----------------
    @Override
    public List<PostResponse> getFeed(Long userId) {
        List<Long> connectedIds = connectionRepository.findConnectedUserIds(userId);
        List<Long> allUserIds = new ArrayList<>(connectedIds);
        allUserIds.add(userId);

        List<Post> posts = postRepository.findByUserIds(allUserIds);
        return posts.stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    // ---------------- Get all posts (admin/debug) ----------------
    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::mapToPostResponse)
                .collect(Collectors.toList());
    }

    // ---------------- Like a post ----------------
    @Override
    public PostResponse likePost(LikeRequest likeRequest) {
        User user = userRepository.findById(likeRequest.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Post post = postRepository.findById(likeRequest.getPostId())
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        // Check if user already liked the post
        if (!likeRepository.existsByPostAndUser(post, user)) {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like); // save like in repository
        }

        return mapToPostResponse(post);
    }

    // ---------------- Comment on a post ----------------
    @Override
    public PostResponse commentOnPost(CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setComment(commentRequest.getComment());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment); // save comment in repository
        return mapToPostResponse(post);
    }

    // ---------------- Helper method to map Post to PostResponse ----------------
    private PostResponse mapToPostResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setPostId(post.getId());
        response.setUserId(post.getUser().getId());
        response.setContent(post.getContent());
        response.setImageUrl(post.getImageUrl());

        // Use repositories to fetch counts efficiently
        response.setLikeCount((int) likeRepository.countByPost(post));
        response.setCommentCount((int) commentRepository.countByPost(post));

        response.setCreatedAt(post.getCreatedAt());
        return response;
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

}
