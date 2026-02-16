package com.devblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devblog.domain.PostLike;

@Mapper
public interface PostLikeMapper {

    // Insert like
    void insert(PostLike postLike);

    // Delete like
    void delete(@Param("postId") Long postId, @Param("userId") Long userId);

    // Count likes for a post
    int countByPostId(Long postId);

    // Find like by post ID and user ID
    PostLike findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
