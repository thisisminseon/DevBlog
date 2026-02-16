package com.devblog.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostImage {

    private Long id;
    private Long postId;

    private String fileName;
    private String filePath;
    private Long fileSize;

    private LocalDateTime createdAt;
    private String thumbnailPath;
}