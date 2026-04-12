package com.example.blog.service;

import com.example.blog.dto.CommentCreateRequest;
import com.example.blog.dto.CommentVO;
import com.example.blog.entity.Comment;
import com.example.blog.exception.BusinessException;
import com.example.blog.mapper.BlogMapper;
import com.example.blog.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentMapper commentMapper;
    private final BlogMapper blogMapper;

    public CommentService(CommentMapper commentMapper, BlogMapper blogMapper) {
        this.commentMapper = commentMapper;
        this.blogMapper = blogMapper;
    }

    public List<CommentVO> listByBlogId(Long blogId) {
        if (blogMapper.selectUserIdById(blogId) == null) {
            throw new BusinessException(404, "博客不存在");
        }
        return commentMapper.selectByBlogId(blogId);
    }

    public CommentVO create(Long userId, Long blogId, CommentCreateRequest req) {
        if (blogMapper.selectUserIdById(blogId) == null) {
            throw new BusinessException(404, "博客不存在");
        }
        Comment comment = new Comment();
        comment.setBlogId(blogId);
        comment.setUserId(userId);
        comment.setContent(req.getContent().trim());
        commentMapper.insert(comment);
        log.info("发表评论: blogId={}, commentId={}", blogId, comment.getId());
        return commentMapper.selectById(comment.getId());
    }
}
