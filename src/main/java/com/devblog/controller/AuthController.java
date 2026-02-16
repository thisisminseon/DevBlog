package com.devblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devblog.domain.User;
import com.devblog.service.UserService;

import lombok.RequiredArgsConstructor;

// Authentication controller (login, signup)
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    // Login page
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "ユーザー名またはパスワードが正しくありません。");
        }
        return "auth/login";
    }

    // Signup page
    @GetMapping("/signup")
    public String signupForm() {
        return "auth/signup";
    }

    // Process signup
    @PostMapping("/signup")
    public String signup(@RequestParam("username") String username,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam("confirmPassword") String confirmPassword,
                         RedirectAttributes redirectAttributes) {

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "パスワードが一致しません。");
            return "redirect:/auth/signup";
        }

        // Check username duplication
        if (userService.isUsernameTaken(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "このユーザー名は既に使用されています。");
            return "redirect:/auth/signup";
        }

        // Check email duplication
        if (userService.isEmailTaken(email)) {
            redirectAttributes.addFlashAttribute("errorMessage", "このメールアドレスは既に登録されています。");
            return "redirect:/auth/signup";
        }

        // Register user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userService.register(user);

        redirectAttributes.addFlashAttribute("successMessage", "会員登録が完了しました。ログインしてください。");
        return "redirect:/auth/login";
    }
}
