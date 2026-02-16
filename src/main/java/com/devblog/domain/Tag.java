package com.devblog.domain;

import lombok.Data;

@Data
public class Tag {

    private Long id;        // tag ID (PK)
    private String name;    // tag name (unique)

    // Transient field for display
    private int postCount;  // number of posts with this tag
}
