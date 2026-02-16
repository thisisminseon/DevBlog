package com.devblog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devblog.domain.PageInfo;
import com.devblog.domain.Post;
import com.devblog.service.PostService;
import com.devblog.service.TagService;

import lombok.RequiredArgsConstructor;

// Main page controller
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final TagService tagService;

    // Main page: public posts with optional tag filter and pagination
    @GetMapping("/hello")
    public String home(@RequestParam(value = "tag", required = false) String tag,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       Model model) {

        List<Post> posts;
        PageInfo pageInfo;

        if (tag != null && !tag.trim().isEmpty()) {
            // Filter by tag
            posts = postService.getPostsByTag(tag, page);
            pageInfo = postService.getPostsByTagPageInfo(tag, page);
            model.addAttribute("currentTag", tag);
        } else {
            // All public posts
            posts = postService.getPublicPosts(page);
            pageInfo = postService.getPublicPostsPageInfo(page);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("allTags", tagService.getAllTags());

        return "index";
    }

}
