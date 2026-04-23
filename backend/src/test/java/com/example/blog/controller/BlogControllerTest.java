package com.example.blog.controller;

import com.example.blog.dto.*;
import com.example.blog.service.BlogService;
import com.example.blog.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void list_shouldReturnBlogList() throws Exception {
        BlogListVO blog1 = new BlogListVO();
        blog1.setId(1L);
        blog1.setTitle("Test Blog 1");
        blog1.setAuthorName("author1");

        BlogListVO blog2 = new BlogListVO();
        blog2.setId(2L);
        blog2.setTitle("Test Blog 2");
        blog2.setAuthorName("author2");

        BlogPageVO pageVO = new BlogPageVO(2, Arrays.asList(blog1, blog2));

        when(blogService.list(1, 10, null)).thenReturn(pageVO);

        mockMvc.perform(get("/api/blogs")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(2))
                .andExpect(jsonPath("$.data.list[0].title").value("Test Blog 1"));
    }

    @Test
    void getById_shouldReturnBlog() throws Exception {
        BlogVO blog = new BlogVO();
        blog.setId(1L);
        blog.setTitle("Test Blog");
        blog.setContent("Test Content");
        blog.setAuthorName("author");

        when(blogService.getById(1L)).thenReturn(blog);

        mockMvc.perform(get("/api/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Blog"));
    }

    @Test
    void list_withAuthorFilter_shouldReturnFilteredList() throws Exception {
        BlogListVO blog = new BlogListVO();
        blog.setId(1L);
        blog.setTitle("Author's Blog");
        blog.setAuthorName("testauthor");

        BlogPageVO pageVO = new BlogPageVO(1, Arrays.asList(blog));

        when(blogService.list(1, 10, "testauthor")).thenReturn(pageVO);

        mockMvc.perform(get("/api/blogs")
                .param("page", "1")
                .param("size", "10")
                .param("author", "testauthor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list[0].authorName").value("testauthor"));
    }

    @Test
    void getComments_shouldReturnCommentList() throws Exception {
        CommentVO comment = new CommentVO();
        comment.setId(1L);
        comment.setContent("Test Comment");
        comment.setAuthorName("commenter");

        when(commentService.listByBlogId(1L)).thenReturn(Arrays.asList(comment));

        mockMvc.perform(get("/api/blogs/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].content").value("Test Comment"));
    }
}
