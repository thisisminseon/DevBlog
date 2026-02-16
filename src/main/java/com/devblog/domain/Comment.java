package com.devblog.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comment {

    private Long id;                // comment ID (PK)
    private Long postId;            // post ID (FK)
    private Long userId;            // user ID (FK)
    private String content;         // comment content
    private String imagePath;       // attached image path (max 1)
    private LocalDateTime createdAt;  // created date
    private LocalDateTime updatedAt;  // updated date

    // Transient fields for display
    private String username;        // from users table JOIN
}
