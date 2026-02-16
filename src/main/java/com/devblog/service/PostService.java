package com.devblog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devblog.domain.PageInfo;
import com.devblog.domain.Post;
import com.devblog.mapper.PostMapper;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import lombok.RequiredArgsConstructor;

// Post CRUD with pagination, search, markdown
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final TagService tagService;
    private final PostViewService postViewService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    private static final int PAGE_SIZE = 10;

    // Markdown parser (thread-safe, reusable)
    private final Parser mdParser = Parser.builder(new MutableDataSet()).build();
    private final HtmlRenderer mdRenderer = HtmlRenderer.builder(new MutableDataSet()).build();

    // Get all posts (legacy)
    public List<Post> getAllPosts() {
        return postMapper.findAll();
    }

    // Get single post by ID
    public Post getPost(Long id) {
        return postMapper.findById(id);
    }

    // Get post detail with all info (user, tags, likes, comments, views, markdown)
    public Post getPostDetail(Long id) {
        Post post = postMapper.findByIdWithUser(id);
        if (post == null) return null;

        // Add view count record
        postViewService.addView(id);

        // Populate transient fields
        post.setTagNames(tagService.getTagNamesByPostId(id));
        post.setViewCount(postViewService.getViewCount(id));
        post.setLikeCount(postLikeService.getLikeCount(id));
        post.setCommentCount(commentService.countByPostId(id));

        // Convert markdown to HTML
        if (post.getContent() != null) {
            Node document = mdParser.parse(post.getContent());
            post.setContentHtml(mdRenderer.render(document));
        }

        return post;
    }

    // Save new post
    public void save(Post post) {
        postMapper.insert(post);
    }

    // Update post
    public void update(Post post) {
        postMapper.update(post);
    }

    // Delete post (check ownership or admin)
    public boolean deletePost(Long postId, Long userId, String userRole) {
        Post post = postMapper.findById(postId);
        if (post == null) return false;

        if (!post.getUserId().equals(userId) && !"ADMIN".equals(userRole)) {
            return false;
        }
        postMapper.deleteById(postId);
        return true;
    }

    // Get public posts with pagination
    public List<Post> getPublicPosts(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Post> posts = postMapper.findPublicPosts(offset, PAGE_SIZE);
        populatePostTags(posts);
        return posts;
    }

    // Get page info for public posts
    public PageInfo getPublicPostsPageInfo(int page) {
        return PageInfo.of(page, postMapper.countPublicPosts(), PAGE_SIZE);
    }

    // Get posts by tag with pagination
    public List<Post> getPostsByTag(String tagName, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Post> posts = postMapper.findByTag(tagName, offset, PAGE_SIZE);
        populatePostTags(posts);
        return posts;
    }

    // Get page info for tag filter
    public PageInfo getPostsByTagPageInfo(String tagName, int page) {
        return PageInfo.of(page, postMapper.countByTag(tagName), PAGE_SIZE);
    }

    // Search posts with pagination
    public List<Post> searchPosts(String keyword, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Post> posts = postMapper.searchPosts(keyword, offset, PAGE_SIZE);
        populatePostTags(posts);
        return posts;
    }

    // Get page info for search
    public PageInfo searchPostsPageInfo(String keyword, int page) {
        return PageInfo.of(page, postMapper.countSearchPosts(keyword), PAGE_SIZE);
    }

    // Get posts by user ID (for my page)
    public List<Post> getPostsByUserId(Long userId, int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Post> posts = postMapper.findByUserId(userId, offset, PAGE_SIZE);
        populatePostTags(posts);
        return posts;
    }

    // Get page info for user posts
    public PageInfo getPostsByUserIdPageInfo(Long userId, int page) {
        return PageInfo.of(page, postMapper.countByUserId(userId), PAGE_SIZE);
    }

    // Get all posts for admin
    public List<Post> getAllPostsForAdmin(int page) {
        int offset = (page - 1) * PAGE_SIZE;
        List<Post> posts = postMapper.findAllForAdmin(offset, PAGE_SIZE);
        populatePostTags(posts);
        return posts;
    }

    // Get page info for admin
    public PageInfo getAllPostsForAdminPageInfo(int page) {
        return PageInfo.of(page, postMapper.countAllForAdmin(), PAGE_SIZE);
    }

    // Count all posts (for admin dashboard)
    public int countAllPosts() {
        return postMapper.countAllForAdmin();
    }

    // Populate tag names for post list display
    private void populatePostTags(List<Post> posts) {
        for (Post post : posts) {
            post.setTagNames(tagService.getTagNamesByPostId(post.getId()));
        }
    }
}
