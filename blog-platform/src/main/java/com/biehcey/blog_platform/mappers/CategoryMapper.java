package com.biehcey.blog_platform.mappers;

import com.biehcey.blog_platform.domain.PostStatus;
import com.biehcey.blog_platform.domain.dtos.CategoryDto;
import com.biehcey.blog_platform.domain.dtos.CreateCategoryRequest;
import com.biehcey.blog_platform.domain.entities.Category;
import com.biehcey.blog_platform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest createCategoryRequest);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts){
        if(null == posts)
            return 0;
        return posts.stream().filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}
