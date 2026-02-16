package com.devblog.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostLike {

    private Long id;                // like ID (PK)
    private Long postId;            // post ID (FK)
    private Long userId;            // user ID (FK)
    private LocalDateTime createdAt;  // created date
}
