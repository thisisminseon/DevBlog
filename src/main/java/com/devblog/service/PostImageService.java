package com.devblog.service;

import net.coobird.thumbnailator.Thumbnails;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devblog.domain.PostImage;
import com.devblog.mapper.PostImageMapper;

import lombok.RequiredArgsConstructor;

// Image upload, thumbnail generation
@Service
@RequiredArgsConstructor
public class PostImageService {

    private final PostImageMapper postImageMapper;

    @Value("${file.upload.path}")
    private String uploadDir;

    // Get images for a post
    public List<PostImage> getImagesByPostId(Long postId) {
        return postImageMapper.findByPostId(postId);
    }

    // Save images with thumbnails
    public void saveImages(Long postId, MultipartFile[] files) {

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            for (MultipartFile file : files) {

                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                String savedName = uuid + "_" + originalName;

                File savedFile = new File(uploadDir + "/" + savedName);
                file.transferTo(savedFile);

                // Generate thumbnail (300x300)
                String thumbnailName = "thumb_" + savedName;
                File thumbnailFile = new File(uploadDir + "/" + thumbnailName);

                Thumbnails.of(savedFile)
                        .size(300, 300)
                        .toFile(thumbnailFile);

                PostImage image = new PostImage();
                image.setPostId(postId);
                image.setFileName(savedName);
                image.setFilePath("/upload/" + savedName);
                image.setFileSize(file.getSize());
                image.setThumbnailPath("/upload/" + thumbnailName);

                postImageMapper.insert(image);
            }

        } catch (IOException e) {
            throw new RuntimeException("File save failed", e);
        }
    }

    // Replace all images for a post
    public void replaceImages(Long postId, MultipartFile[] files) {
        // Delete existing images from DB
        postImageMapper.deleteByPostId(postId);
        // Save new images
        saveImages(postId, files);
    }
}
