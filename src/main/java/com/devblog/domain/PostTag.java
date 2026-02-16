package com.devblog.domain;

import lombok.Data;

@Data
public class PostTag {

    private Long postId;    // post ID (composite PK)
    private Long tagId;     // tag ID (composite PK)
}
