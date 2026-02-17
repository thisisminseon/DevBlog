package com.devblog.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devblog.config.CustomUserDetails;
import com.devblog.service.PostLikeService;

import lombok.RequiredArgsConstructor;

// Like toggle REST API
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final PostLikeService postLikeService;

    // Toggle like (returns {liked: true/false, count: N})
    @PostMapping("/toggle/{postId}")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> result = postLikeService.toggleLike(postId, userDetails.getUserId());
        return ResponseEntity.ok(result);
    }
}
