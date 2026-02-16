package com.devblog.config;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.devblog.domain.Tag;
import com.devblog.service.TagService;

import lombok.RequiredArgsConstructor;

// Inject popular tags into all templates (for navigation)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final TagService tagService;

    // Top 15 popular tags for navigation bar
    @ModelAttribute("navTags")
    public List<Tag> navTags() {
        return tagService.getPopularTags(15);
    }
}
