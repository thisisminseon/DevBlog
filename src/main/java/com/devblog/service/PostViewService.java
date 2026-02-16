package com.devblog.service;

import org.springframework.stereotype.Service;

import com.devblog.mapper.PostViewMapper;

import lombok.RequiredArgsConstructor;

// Post view count management (using post_view table)
@Service
@RequiredArgsConstructor
public class PostViewService {

    private final PostViewMapper postViewMapper;

    // Add a view record
    public void addView(Long postId) {
        postViewMapper.insert(postId);
    }

    // Get total view count for a post
    public int getViewCount(Long postId) {
        return postViewMapper.countByPostId(postId);
    }
}
