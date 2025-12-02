package com.biehcey.blog_platform.repositories;

import com.biehcey.blog_platform.domain.PostStatus;
import com.biehcey.blog_platform.domain.entities.Category;
import com.biehcey.blog_platform.domain.entities.Post;
import com.biehcey.blog_platform.domain.entities.Tag;
import com.biehcey.blog_platform.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsIsContaining(PostStatus status, Category category, Tag tag);
    List<Post> findAllByStatusAndCategory(PostStatus status, Category category);
    List<Post> findAllByStatusAndTagsIsContaining(PostStatus status, Tag tag);
    List<Post> findAllByStatus(PostStatus status);
    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
