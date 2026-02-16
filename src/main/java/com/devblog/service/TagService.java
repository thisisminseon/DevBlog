package com.devblog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.devblog.domain.PostTag;
import com.devblog.domain.Tag;
import com.devblog.mapper.PostTagMapper;
import com.devblog.mapper.TagMapper;

import lombok.RequiredArgsConstructor;

// Tag management and post-tag sync
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagMapper tagMapper;
    private final PostTagMapper postTagMapper;

    // Get all tags
    public List<Tag> getAllTags() {
        return tagMapper.findAll();
    }

    // Get popular tags (top N)
    public List<Tag> getPopularTags(int limit) {
        return tagMapper.findPopular(limit);
    }

    // Get tags for a specific post
    public List<Tag> getTagsByPostId(Long postId) {
        return tagMapper.findByPostId(postId);
    }

    // Get tag names for a specific post
    public List<String> getTagNamesByPostId(Long postId) {
        return tagMapper.findByPostId(postId).stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    // Sync post tags: delete old, create/link new
    public void syncPostTags(Long postId, List<String> tagNames) {
        // Remove existing tags for this post
        postTagMapper.deleteByPostId(postId);

        if (tagNames == null || tagNames.isEmpty()) return;

        for (String name : tagNames) {
            String trimmed = name.trim();
            if (trimmed.isEmpty()) continue;

            // Remove # prefix if present
            if (trimmed.startsWith("#")) {
                trimmed = trimmed.substring(1).trim();
            }
            if (trimmed.isEmpty()) continue;

            // Find or create tag
            Tag tag = tagMapper.findByName(trimmed);
            if (tag == null) {
                tag = new Tag();
                tag.setName(trimmed);
                tagMapper.insert(tag);
            }

            // Link post to tag
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tag.getId());
            postTagMapper.insert(postTag);
        }
    }

    // Parse comma-separated tag string into list
    public List<String> parseTags(String tagString) {
        List<String> tags = new ArrayList<>();
        if (tagString == null || tagString.trim().isEmpty()) return tags;

        String[] parts = tagString.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                tags.add(trimmed);
            }
        }
        return tags;
    }
}
