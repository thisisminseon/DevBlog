package com.devblog.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PostView {

    private Long id;                    // view ID (PK)
    private Long postId;                // post ID (FK)
    private LocalDateTime viewedAt;     // viewed timestamp
}
