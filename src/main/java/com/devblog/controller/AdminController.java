package com.devblog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devblog.domain.PageInfo;
import com.devblog.domain.Post;
import com.devblog.domain.User;
import com.devblog.mapper.PostMapper;
import com.devblog.mapper.UserMapper;
import com.devblog.service.PostService;
import com.devblog.service.UserService;

import lombok.RequiredArgsConstructor;

// Admin dashboard controller (ADMIN role required)
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PostService postService;
    private final UserMapper userMapper;
    private final PostMapper postMapper;

    // Admin dashboard main page
    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("userCount", userService.countAll());
        model.addAttribute("postCount", postService.countAllPosts());

        return "admin/index";
    }

    // User management page
    @GetMapping("/users")
    public String userList(Model model) {

        List<User> users = userMapper.findAll();
        model.addAttribute("users", users);

        return "admin/users";
    }

    // Change user role
    @PostMapping("/users/{id}/role")
    public String changeUserRole(@PathVariable("id") Long id,
                                 @RequestParam("role") String role,
                                 RedirectAttributes redirectAttributes) {

        userMapper.updateRole(id, role);
        redirectAttributes.addFlashAttribute("successMessage", "ユーザー権限を変更しました。");

        return "redirect:/admin/users";
    }

    // Post management page
    @GetMapping("/posts")
    public String postList(@RequestParam(value = "page", defaultValue = "1") int page,
                           Model model) {

        List<Post> posts = postService.getAllPostsForAdmin(page);
        PageInfo pageInfo = postService.getAllPostsForAdminPageInfo(page);

        model.addAttribute("posts", posts);
        model.addAttribute("pageInfo", pageInfo);

        return "admin/posts";
    }

    // Toggle post visibility (public/private)
    @PostMapping("/posts/{id}/toggle-visibility")
    public String toggleVisibility(@PathVariable("id") Long id,
                                   RedirectAttributes redirectAttributes) {

        postMapper.toggleVisibility(id);
        redirectAttributes.addFlashAttribute("successMessage", "投稿の公開状態を変更しました。");

        return "redirect:/admin/posts";
    }
}
