package com.biehcey.blog_platform.services.impl;

import com.biehcey.blog_platform.domain.CreatePostRequest;
import com.biehcey.blog_platform.domain.PostStatus;
import com.biehcey.blog_platform.domain.UpdatePostRequest;
import com.biehcey.blog_platform.domain.entities.Category;
import com.biehcey.blog_platform.domain.entities.Post;
import com.biehcey.blog_platform.domain.entities.Tag;
import com.biehcey.blog_platform.domain.entities.User;
import com.biehcey.blog_platform.repositories.PostRepository;
import com.biehcey.blog_platform.services.CategoryService;
import com.biehcey.blog_platform.services.PostService;
import com.biehcey.blog_platform.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if(categoryId != null && tagId != null){
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsIsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        if(categoryId != null){
            Category category = categoryService.getCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }
        if(tagId != null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsIsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }
        return postRepository.findAllByStatus(
                PostStatus.PUBLISHED
        );
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(
                user,
                PostStatus.DRAFT
        );
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));

        Category category = categoryService.getCategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);

        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with ID " + id));

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));


        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Category newCategory = categoryService.getCategoryById(updatePostRequest.getCategoryId());
            existingPost.setCategory(newCategory);
        }
        Set<UUID> existingTagIds = existingPost.getTags().stream()
                .map(Tag::getId).collect(Collectors.toSet());
        Set<UUID> updateTagIds = updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updateTagIds)){
            List<Tag> newTags = tagService.getTagByIds(updateTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }
        return postRepository.save(existingPost);
    }

    @Override
    public Post getPost(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with ID " + id));
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPost(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content){
        if(content == null || content.isEmpty()){
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}
