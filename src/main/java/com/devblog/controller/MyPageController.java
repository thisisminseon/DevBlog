package com.devblog.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devblog.config.CustomUserDetails;
import com.devblog.domain.PageInfo;
import com.devblog.domain.Post;
import com.devblog.domain.User;
import com.devblog.service.PostService;
import com.devblog.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

// My page controller (profile, my posts)
@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;
    private final PostService postService;

    // My page: profile info + my posts list
    @GetMapping
    public String myPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         Model model) {

        User user = userService.findById(userDetails.getUserId());
        List<Post> posts = postService.getPostsByUserId(userDetails.getUserId(), page);
        PageInfo pageInfo = postService.getPostsByUserIdPageInfo(userDetails.getUserId(), page);

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("pageInfo", pageInfo);

        return "mypage/index";
    }

    // Edit profile form
    @GetMapping("/edit-profile")
    public String editProfileForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model) {

        User user = userService.findById(userDetails.getUserId());
        model.addAttribute("user", user);

        return "mypage/edit-profile";
    }

    // Update profile
    @PostMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestParam("username") String username,
                              @RequestParam("email") String email,
                              RedirectAttributes redirectAttributes) {

        User currentUser = userService.findById(userDetails.getUserId());

        // Check username duplication (except current user)
        if (!currentUser.getUsername().equals(username) && userService.isUsernameTaken(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "このユーザー名は既に使用されています。");
            return "redirect:/mypage/edit-profile";
        }

        // Check email duplication (except current user)
        if (!currentUser.getEmail().equals(email) && userService.isEmailTaken(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "このメールアドレスは既に登録されています。");
            return "redirect:/mypage/edit-profile";
        }

        currentUser.setUsername(username);
        currentUser.setEmail(email);
        userService.updateProfile(currentUser);

        redirectAttributes.addFlashAttribute("successMessage", "プロフィールを更新しました。");
        return "redirect:/mypage";
    }

    // Change password
    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "新しいパスワードが一致しません。");
            return "redirect:/mypage/edit-profile";
        }

        boolean changed = userService.changePassword(userDetails.getUserId(),
                currentPassword, newPassword);

        if (!changed) {
            redirectAttributes.addFlashAttribute("errorMessage", "現在のパスワードが正しくありません。");
            return "redirect:/mypage/edit-profile";
        }

        redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。");
        return "redirect:/mypage";
    }

    // Delete account
    @PostMapping("/delete-account")
    public String deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                HttpServletRequest request) {

        userService.deleteAccount(userDetails.getUserId());

        // Invalidate session and clear security context
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/hello";
    }
}
