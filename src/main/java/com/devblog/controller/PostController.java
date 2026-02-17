package com.devblog.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devblog.config.CustomUserDetails;
import com.devblog.domain.PageInfo;
import com.devblog.domain.Post;
import com.devblog.service.CommentService;
import com.devblog.service.PostImageService;
import com.devblog.service.PostLikeService;
import com.devblog.service.PostService;
import com.devblog.service.TagService;

import lombok.RequiredArgsConstructor;

// Post CRUD controller with auth, tags, search
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostImageService postImageService;
    private final TagService tagService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    // Post detail page
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model) {

        Post post = postService.getPostDetail(id);
        if (post == null) {
            return "redirect:/hello";
        }

        // Private post: only owner or admin can view
        if (post.getIsPrivate() != null && post.getIsPrivate()) {
            if (userDetails == null) {
                return "redirect:/hello";
            }
            boolean isOwner = post.getUserId() != null && post.getUserId().equals(userDetails.getUserId());
            boolean isAdmin = "ADMIN".equals(userDetails.getUser().getRole());
            if (!isOwner && !isAdmin) {
                return "redirect:/hello";
            }
        }

        model.addAttribute("post", post);
        model.addAttribute("images", postImageService.getImagesByPostId(id));
        model.addAttribute("comments", commentService.getCommentsByPostId(id));

        // Like status for logged-in user
        if (userDetails != null) {
            model.addAttribute("liked", postLikeService.isLikedByUser(id, userDetails.getUserId()));
            model.addAttribute("isOwner", post.getUserId() != null
                    && post.getUserId().equals(userDetails.getUserId()));
        } else {
            model.addAttribute("liked", false);
            model.addAttribute("isOwner", false);
        }

        return "post/detail";
    }

    // Write form page
    @GetMapping("/write")
    public String writeForm() {
        return "post/write";
    }

    // Save new post
    @PostMapping("/write")
    public String write(Post post,
                        @RequestParam(value = "tags", required = false) String tags,
                        @RequestParam(value = "images", required = false) MultipartFile[] images,
                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Set user info
        post.setUserId(userDetails.getUserId());
        post.setAuthor(userDetails.getDisplayName());

        // Default to public if isPrivate is null
        if (post.getIsPrivate() == null) {
            post.setIsPrivate(false);
        }

        // Filter out empty files
        boolean hasImages = images != null && images.length > 0
                && !(images.length == 1 && images[0].isEmpty());

        // Max 10 images check
        if (hasImages && images.length > 10) {
            throw new IllegalArgumentException("画像は最大で10枚までアップロードできます。");
        }

        // Save post first
        postService.save(post);

        // Sync tags
        if (tags != null && !tags.trim().isEmpty()) {
            List<String> tagList = tagService.parseTags(tags);
            tagService.syncPostTags(post.getId(), tagList);
        }

        // Save images
        if (hasImages) {
            postImageService.saveImages(post.getId(), images);
        }

        return "redirect:/post/detail/" + post.getId();
    }

    // Edit form page
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {

        Post post = postService.getPost(id);
        if (post == null) {
            return "redirect:/hello";
        }

        // Ownership check
        if (!post.getUserId().equals(userDetails.getUserId())
                && !"ADMIN".equals(userDetails.getUser().getRole())) {
            return "redirect:/post/detail/" + id;
        }

        model.addAttribute("post", post);
        model.addAttribute("images", postImageService.getImagesByPostId(id));
        model.addAttribute("postTags", String.join(", ", tagService.getTagNamesByPostId(id)));

        return "post/edit";
    }

    // Update post
    @PostMapping("/edit")
    public String edit(Post post,
                       @RequestParam(value = "tags", required = false) String tags,
                       @RequestParam(value = "images", required = false) MultipartFile[] images,
                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Post existing = postService.getPost(post.getId());
        if (existing == null) {
            return "redirect:/hello";
        }

        // Ownership check
        if (!existing.getUserId().equals(userDetails.getUserId())
                && !"ADMIN".equals(userDetails.getUser().getRole())) {
            return "redirect:/post/detail/" + post.getId();
        }

        // Default to public if isPrivate is null
        if (post.getIsPrivate() == null) {
            post.setIsPrivate(false);
        }

        // Filter out empty files
        boolean hasImages = images != null && images.length > 0
                && !(images.length == 1 && images[0].isEmpty());

        // Max 10 images check
        if (hasImages && images.length > 10) {
            throw new IllegalArgumentException("画像は最大で10枚までアップロードできます。");
        }

        // Update post
        post.setUserId(existing.getUserId());
        postService.update(post);

        // Sync tags
        List<String> tagList = tagService.parseTags(tags);
        tagService.syncPostTags(post.getId(), tagList);

        // Update images if uploaded
        if (hasImages) {
            postImageService.replaceImages(post.getId(), images);
        }

        return "redirect:/post/detail/" + post.getId();
    }

    // Delete post
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         RedirectAttributes redirectAttributes) {

        boolean deleted = postService.deletePost(id, userDetails.getUserId(),
                userDetails.getUser().getRole());

        if (!deleted) {
            redirectAttributes.addFlashAttribute("errorMessage", "投稿を削除できませんでした。");
            return "redirect:/post/detail/" + id;
        }

        return "redirect:/hello";
    }

    // Search posts
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false) String keyword,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         Model model) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return "redirect:/hello";
        }

        List<Post> posts = postService.searchPosts(keyword, page);
        PageInfo pageInfo = postService.searchPostsPageInfo(keyword, page);

        model.addAttribute("posts", posts);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("keyword", keyword);

        return "post/search";
    }
}
