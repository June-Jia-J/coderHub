package com.example.blog.controller;

import com.example.blog.dto.BlogCreateRequest;
import com.example.blog.dto.BlogPageVO;
import com.example.blog.dto.BlogVO;
import com.example.blog.dto.CommentCreateRequest;
import com.example.blog.dto.CommentVO;
import com.example.blog.dto.CommonResponse;
import com.example.blog.service.BlogService;
import com.example.blog.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;
    private final CommentService commentService;

    public BlogController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @GetMapping
    public CommonResponse<BlogPageVO> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String author) {
        BlogPageVO data = blogService.list(page, size, author);
        return CommonResponse.ok(data);
    }

    @GetMapping("/{id}")
    public CommonResponse<BlogVO> getById(@PathVariable Long id) {
        BlogVO data = blogService.getById(id);
        return CommonResponse.ok(data);
    }

    @PostMapping
    public CommonResponse<BlogVO> create(
            @Valid @RequestBody BlogCreateRequest req,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        BlogVO data = blogService.create(userId, req);
        return CommonResponse.ok(data);
    }

    @PutMapping("/{id}")
    public CommonResponse<BlogVO> update(
            @PathVariable Long id,
            @Valid @RequestBody BlogCreateRequest req,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        BlogVO data = blogService.update(userId, id, req);
        return CommonResponse.ok(data);
    }

    @DeleteMapping("/{id}")
    public CommonResponse<Void> delete(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        blogService.delete(userId, id);
        return CommonResponse.ok(null);
    }

    @GetMapping("/{id}/comments")
    public CommonResponse<List<CommentVO>> getComments(@PathVariable Long id) {
        List<CommentVO> list = commentService.listByBlogId(id);
        return CommonResponse.ok(list);
    }

    @PostMapping("/{id}/comments")
    public CommonResponse<CommentVO> createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentCreateRequest req,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        CommentVO data = commentService.create(userId, id, req);
        return CommonResponse.ok(data);
    }
}
