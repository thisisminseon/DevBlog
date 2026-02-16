package com.devblog.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devblog.domain.Comment;
import com.devblog.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;

// Comment CRUD with image attachment
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    @Value("${file.upload.path}")
    private String uploadDir;

    // Get comments for a post
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }

    // Find comment by ID
    public Comment findById(Long id) {
        return commentMapper.findById(id);
    }

    // Add comment
    public void addComment(Comment comment) {
        commentMapper.insert(comment);
    }

    // Update comment (check ownership)
    public boolean updateComment(Long commentId, String content, String imagePath, Long userId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null || !comment.getUserId().equals(userId)) {
            return false;
        }
        comment.setContent(content);
        if (imagePath != null) {
            comment.setImagePath(imagePath);
        }
        commentMapper.update(comment);
        return true;
    }

    // Delete comment (check ownership or admin)
    public boolean deleteComment(Long commentId, Long userId, String userRole) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) return false;

        if (!comment.getUserId().equals(userId) && !"ADMIN".equals(userRole)) {
            return false;
        }
        commentMapper.deleteById(commentId);
        return true;
    }

    // Save comment image (1 image only)
    public String saveCommentImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String uuid = UUID.randomUUID().toString();
            String savedName = uuid + "_" + file.getOriginalFilename();
            File savedFile = new File(uploadDir + "/" + savedName);
            file.transferTo(savedFile);

            return "/upload/" + savedName;
        } catch (IOException e) {
            throw new RuntimeException("Image save failed", e);
        }
    }

    // Count comments by post ID
    public int countByPostId(Long postId) {
        return commentMapper.countByPostId(postId);
    }
}
