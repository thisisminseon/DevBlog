package com.devblog.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devblog.domain.PostLike;
import com.devblog.mapper.PostLikeMapper;

import lombok.RequiredArgsConstructor;

// Like toggle functionality
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeMapper postLikeMapper;

    // Toggle like: add if not exists, remove if exists
    public Map<String, Object> toggleLike(Long postId, Long userId) {
        Map<String, Object> result = new HashMap<>();

        PostLike existing = postLikeMapper.findByPostIdAndUserId(postId, userId);
        if (existing != null) {
            // Unlike
            postLikeMapper.delete(postId, userId);
            result.put("liked", false);
        } else {
            // Like
            PostLike like = new PostLike();
            like.setPostId(postId);
            like.setUserId(userId);
            postLikeMapper.insert(like);
            result.put("liked", true);
        }

        result.put("count", postLikeMapper.countByPostId(postId));
        return result;
    }

    // Get like count for a post
    public int getLikeCount(Long postId) {
        return postLikeMapper.countByPostId(postId);
    }

    // Check if user liked a post
    public boolean isLikedByUser(Long postId, Long userId) {
        return postLikeMapper.findByPostIdAndUserId(postId, userId) != null;
    }
}
