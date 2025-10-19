package com.Alumni.RGUKT_DIALOGUE.controller;

import com.Alumni.RGUKT_DIALOGUE.dto.*;
import com.Alumni.RGUKT_DIALOGUE.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * Endpoints:
 * POST   /api/posts/create        -> create a new post
 * GET    /api/posts/user/{userId} -> get all posts created by a user
 * GET    /api/posts/feed/{userId} -> get personalized feed for a user
 * GET    /api/posts/all            -> get all posts (admin/debug)
 * POST   /api/posts/like           -> like a post
 * POST   /api/posts/comment        -> comment on a post
 */


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService postService;

    // ---------------- Create a post ----------------
    @PostMapping("/create")
    public PostResponse createPost(@RequestBody PostRequest postRequest) {
        return postService.createPost(postRequest);
    }

    // ---------------- View all posts by a user ----------------
    @GetMapping("/user/{userId}")
    public List<PostResponse> getUserPosts(@PathVariable Long userId) {
        return postService.getUserPosts(userId);
    }

    // ---------------- Get personalized feed ----------------
    @GetMapping("/feed/{userId}")
    public List<PostResponse> getFeed(@PathVariable Long userId) {
        return postService.getFeed(userId);
    }

    // ---------------- Get all posts (admin/debug) ----------------
    @GetMapping("/all")
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    // ---------------- Like a post ----------------
    @PostMapping("/like")
    public PostResponse likePost(@RequestBody LikeRequest likeRequest) {
        return postService.likePost(likeRequest);
    }

    // ---------------- Comment on a post ----------------
    @PostMapping("/comment")
    public PostResponse commentOnPost(@RequestBody CommentRequest commentRequest) {
        return postService.commentOnPost(commentRequest);
    }
}
