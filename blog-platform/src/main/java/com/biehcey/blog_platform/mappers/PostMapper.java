package com.biehcey.blog_platform.mappers;

import com.biehcey.blog_platform.domain.CreatePostRequest;
import com.biehcey.blog_platform.domain.UpdatePostRequest;
import com.biehcey.blog_platform.domain.dtos.CreatePostRequestDto;
import com.biehcey.blog_platform.domain.dtos.PostDto;
import com.biehcey.blog_platform.domain.dtos.UpdatePostRequestDto;
import com.biehcey.blog_platform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    Post toEntity(PostDto postDto);
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostDto toDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
