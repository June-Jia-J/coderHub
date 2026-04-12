package com.example.blog.service;

import com.example.blog.dto.BlogCreateRequest;
import com.example.blog.dto.BlogListVO;
import com.example.blog.dto.BlogPageVO;
import com.example.blog.dto.BlogVO;
import com.example.blog.entity.Blog;
import com.example.blog.exception.BusinessException;
import com.example.blog.mapper.BlogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private static final Logger log = LoggerFactory.getLogger(BlogService.class);

    private final BlogMapper blogMapper;

    public BlogService(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    public BlogPageVO list(int page, int size, String author) {
        int offset = (page - 1) * size;
        int total = blogMapper.count(author);
        List<BlogListVO> list = blogMapper.selectPage(offset, size, author);
        return new BlogPageVO(total, list);
    }

    public BlogVO getById(Long id) {
        BlogVO vo = blogMapper.selectById(id);
        if (vo == null) {
            throw new BusinessException(404, "博客不存在");
        }
        return vo;
    }

    public BlogVO create(Long userId, BlogCreateRequest req) {
        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle(req.getTitle());
        blog.setContent(req.getContent());
        blogMapper.insert(blog);
        log.info("创建博客: id={}, title={}", blog.getId(), blog.getTitle());
        return getById(blog.getId());
    }

    public BlogVO update(Long userId, Long id, BlogCreateRequest req) {
        BlogVO existing = getById(id);
        if (!existing.getAuthorId().equals(userId)) {
            throw new BusinessException(403, "无权限编辑该博客");
        }
        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle(req.getTitle());
        blog.setContent(req.getContent());
        blogMapper.update(blog);
        log.info("更新博客: id={}", id);
        return getById(id);
    }

    public void delete(Long userId, Long id) {
        Long authorId = blogMapper.selectUserIdById(id);
        if (authorId == null) {
            throw new BusinessException(404, "博客不存在");
        }
        if (!authorId.equals(userId)) {
            throw new BusinessException(403, "无权限删除该博客");
        }
        blogMapper.deleteById(id);
        log.info("删除博客: id={}", id);
    }
}
