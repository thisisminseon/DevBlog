package com.devblog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.devblog.domain.PostImage;

@Mapper
public interface PostImageMapper {

    void insert(PostImage postImage);

    int countByPostId(Long postId);
    
    List<PostImage> findByPostId(Long postId);
    
    void deleteByPostId(Long postId);
}