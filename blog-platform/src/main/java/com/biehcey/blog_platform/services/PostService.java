package com.biehcey.blog_platform.services;

import com.biehcey.blog_platform.domain.CreatePostRequest;
import com.biehcey.blog_platform.domain.UpdatePostRequest;
import com.biehcey.blog_platform.domain.entities.Post;
import com.biehcey.blog_platform.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
    Post updatePost(UUID id, UpdatePostRequest updatePostRequest);
    Post getPost(UUID id);
    void deletePost(UUID id);
}
