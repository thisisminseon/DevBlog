package com.devblog.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {

    private Long id;                // user ID (PK)
    private String loginId;         // login ID (unique, for authentication)
    private String username;        // display name
    private String email;           // email (unique)
    private String password;        // bcrypt hashed password
    private String role;            // role: USER or ADMIN
    private LocalDateTime createdAt;  // created date
    private LocalDateTime updatedAt;  // updated date
}
