package com.devblog.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Post {

    private Long id;            // post ID (PK)
    private Long userId;        // user ID (FK to users)
    private Long categoryId;    // category ID (FK, not used)
    private String title;       // title
    private String content;     // content (markdown)
    private String description; // short excerpt for card display
    private String author;      // author name (legacy)
    private Boolean isPrivate;  // public/private flag
    private LocalDateTime createdAt;  // created date
    private LocalDateTime updatedAt;  // updated date

    // Transient fields for display (populated by service/query)
    private String username;          // from users table JOIN
    private String thumbnailPath;     // first image thumbnail
    private List<String> tagNames;    // tag names for this post
    private int likeCount;            // total likes
    private int commentCount;         // total comments
    private int viewCount;            // total views (from post_view)
    private String contentHtml;       // markdown converted to HTML
}
