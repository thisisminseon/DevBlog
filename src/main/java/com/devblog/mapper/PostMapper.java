package com.devblog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devblog.domain.Post;

@Mapper
public interface PostMapper {

    // Get all posts (legacy)
    List<Post> findAll();

    // Insert new post
    void insert(Post post);

    // Find post by ID
    Post findById(Long id);

    // Find post by ID with user info (JOIN)
    Post findByIdWithUser(Long id);

    // Update post
    void update(Post post);

    // Delete post
    void deleteById(Long id);

    // Find public posts with pagination
    List<Post> findPublicPosts(@Param("offset") int offset, @Param("limit") int limit);

    // Count total public posts
    int countPublicPosts();

    // Find posts by tag name with pagination
    List<Post> findByTag(@Param("tagName") String tagName,
                         @Param("offset") int offset, @Param("limit") int limit);

    // Count posts by tag name
    int countByTag(@Param("tagName") String tagName);

    // Search posts by keyword with pagination
    List<Post> searchPosts(@Param("keyword") String keyword,
                           @Param("offset") int offset, @Param("limit") int limit);

    // Count search results
    int countSearchPosts(@Param("keyword") String keyword);

    // Find posts by user ID with pagination
    List<Post> findByUserId(@Param("userId") Long userId,
                            @Param("offset") int offset, @Param("limit") int limit);

    // Count posts by user ID
    int countByUserId(@Param("userId") Long userId);

    // Find all posts for admin (including private)
    List<Post> findAllForAdmin(@Param("offset") int offset, @Param("limit") int limit);

    // Count all posts for admin
    int countAllForAdmin();

    // Toggle post visibility (for admin)
    void toggleVisibility(Long id);
}
