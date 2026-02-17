package com.devblog.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devblog.domain.User;
import com.devblog.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

// User registration, profile management
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    // Register new user
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userMapper.insert(user);
    }

    // Find user by ID
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    // Find user by username
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    // Find user by email
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    // Check if loginId already exists
    public boolean isLoginIdTaken(String loginId) {
        return userMapper.findByLoginId(loginId) != null;
    }

    // Check if username already exists
    public boolean isUsernameTaken(String username) {
        return userMapper.findByUsername(username) != null;
    }

    // Check if email already exists
    public boolean isEmailTaken(String email) {
        return userMapper.findByEmail(email) != null;
    }

    // Update user profile (username, email)
    public void updateProfile(User user) {
        userMapper.update(user);
    }

    // Change password
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) return false;

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(user);
        return true;
    }

    // Delete account
    public void deleteAccount(Long userId) {
        userMapper.deleteById(userId);
    }

    // Count all users (for admin)
    public int countAll() {
        return userMapper.countAll();
    }
}
