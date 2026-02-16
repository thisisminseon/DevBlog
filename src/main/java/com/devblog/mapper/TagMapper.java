package com.devblog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devblog.domain.Tag;

@Mapper
public interface TagMapper {

    // Find tag by ID
    Tag findById(Long id);

    // Find tag by name
    Tag findByName(String name);

    // Insert new tag
    void insert(Tag tag);

    // Find all tags
    List<Tag> findAll();

    // Find tags by post ID
    List<Tag> findByPostId(Long postId);

    // Find popular tags (top N by post count)
    List<Tag> findPopular(@Param("limit") int limit);
}
