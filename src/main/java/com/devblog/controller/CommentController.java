package com.devblog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devblog.config.CustomUserDetails;
import com.devblog.domain.Comment;
import com.devblog.service.CommentService;

import lombok.RequiredArgsConstructor;

// Comment CRUD controller
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // Add comment (form submit, redirect back to post)
    @PostMapping("/add")
    public String addComment(@RequestParam("postId") Long postId,
                             @RequestParam("content") String content,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             RedirectAttributes redirectAttributes) {

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userDetails.getUserId());
        comment.setContent(content);

        // Save image if uploaded
        if (image != null && !image.isEmpty()) {
            String imagePath = commentService.saveCommentImage(image);
            comment.setImagePath(imagePath);
        }

        commentService.addComment(comment);
        return "redirect:/post/detail/" + postId;
    }

    // Delete comment (AJAX)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> result = new HashMap<>();

        boolean deleted = commentService.deleteComment(id, userDetails.getUserId(),
                userDetails.getUser().getRole());

        if (deleted) {
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("message", "コメントを削除できませんでした。");
        }

        return ResponseEntity.ok(result);
    }

    // Edit comment (AJAX)
    @PostMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editComment(
            @PathVariable Long id,
            @RequestParam("content") String content,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> result = new HashMap<>();

        boolean updated = commentService.updateComment(id, content, null,
                userDetails.getUserId());

        if (updated) {
            result.put("success", true);
        } else {
            result.put("success", false);
            result.put("message", "コメントを編集できませんでした。");
        }

        return ResponseEntity.ok(result);
    }
}
