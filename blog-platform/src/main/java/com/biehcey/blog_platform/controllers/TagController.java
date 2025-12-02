package com.biehcey.blog_platform.controllers;

import com.biehcey.blog_platform.domain.dtos.CreateTagsRequest;
import com.biehcey.blog_platform.domain.dtos.TagResponse;
import com.biehcey.blog_platform.domain.entities.Tag;
import com.biehcey.blog_platform.mappers.TagMapper;
import com.biehcey.blog_platform.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags(){
        List<Tag> tags = tagService.getTags();
        List<TagResponse> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(tagResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@RequestBody CreateTagsRequest tagsRequest){
        List<Tag> savedTags = tagService.createTags(tagsRequest.getNames());
        List<TagResponse> tagResponses = savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(tagResponses, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();

        //2:53
    }
}
