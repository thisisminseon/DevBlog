package com.devblog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devblog.domain.Comment;

@Mapper
public interface CommentMapper {

    // Find comments by post ID (with username from users JOIN)
    List<Comment> findByPostId(Long postId);

    // Find comment by ID
    Comment findById(Long id);

    // Insert new comment
    void insert(Comment comment);

    // Update comment
    void update(Comment comment);

    // Delete comment
    void deleteById(Long id);

    // Count comments by post ID
    int countByPostId(Long postId);
}
