package com.devblog.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostViewMapper {

    // Insert view record
    void insert(Long postId);

    // Count views for a post
    int countByPostId(Long postId);
}
