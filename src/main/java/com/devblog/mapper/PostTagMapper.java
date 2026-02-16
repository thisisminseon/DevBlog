package com.devblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devblog.domain.PostTag;

@Mapper
public interface PostTagMapper {

    // Insert post-tag relation
    void insert(PostTag postTag);

    // Delete all tags for a post
    void deleteByPostId(Long postId);
}
