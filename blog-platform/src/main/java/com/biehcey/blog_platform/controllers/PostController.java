package com.biehcey.blog_platform.controllers;

import com.biehcey.blog_platform.domain.CreatePostRequest;
import com.biehcey.blog_platform.domain.UpdatePostRequest;
import com.biehcey.blog_platform.domain.dtos.CreatePostRequestDto;
import com.biehcey.blog_platform.domain.dtos.PostDto;
import com.biehcey.blog_platform.domain.dtos.UpdatePostRequestDto;
import com.biehcey.blog_platform.domain.entities.Post;
import com.biehcey.blog_platform.domain.entities.User;
import com.biehcey.blog_platform.mappers.PostMapper;
import com.biehcey.blog_platform.services.PostService;
import com.biehcey.blog_platform.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(
            @RequestParam(required = false)UUID categoryId,
            @RequestParam(required = false)UUID tagId ){
        List<Post> allPosts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = allPosts.stream()
                .map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId){
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        List<PostDto> postDtos = draftPosts.stream()
                .map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
            ) {
        User loggedInUser = userService.getUserById(userId);
        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);
        Post createdPost = postService.createPost(loggedInUser, createPostRequest);
        PostDto createdPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto
            ) {
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post updatedPost = postService.updatePost(id, updatePostRequest);
        PostDto postDto = postMapper.toDto(updatedPost);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(
            @PathVariable UUID id
    ) {
        Post post = postService.getPost(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID id
    ){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
