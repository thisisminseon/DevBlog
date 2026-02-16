package com.devblog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.devblog.domain.User;

@Mapper
public interface UserMapper {

    // Find user by ID
    User findById(Long id);

    // Find user by username (for login)
    User findByUsername(String username);

    // Find user by email
    User findByEmail(String email);

    // Insert new user
    void insert(User user);

    // Update user info
    void update(User user);

    // Delete user
    void deleteById(Long id);

    // Find all users (for admin)
    List<User> findAll();

    // Update user role (for admin)
    void updateRole(@Param("id") Long id, @Param("role") String role);

    // Count all users (for admin)
    int countAll();
}
