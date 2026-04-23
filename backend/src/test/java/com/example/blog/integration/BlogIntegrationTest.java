package com.example.blog.integration;

import com.example.blog.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlogIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullBlogWorkflow_shouldWork() throws Exception {
        // 1. 注册用户
        RegisterRequest registerReq = new RegisterRequest();
        registerReq.setUsername("integrationuser");
        registerReq.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 2. 登录获取 token
        LoginRequest loginReq = new LoginRequest();
        loginReq.setUsername("integrationuser");
        loginReq.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        CommonResponse<LoginResponse> loginResponse = objectMapper.readValue(responseBody,
                objectMapper.getTypeFactory().constructParametricType(CommonResponse.class, LoginResponse.class));
        String token = loginResponse.getData().getToken();

        // 3. 创建博客
        BlogCreateRequest blogReq = new BlogCreateRequest();
        blogReq.setTitle("Integration Test Blog");
        blogReq.setContent("This is a test blog content for integration testing.");

        MvcResult createResult = mockMvc.perform(post("/api/blogs")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blogReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andReturn();

        String createBody = createResult.getResponse().getContentAsString();
        CommonResponse<BlogVO> blogResponse = objectMapper.readValue(createBody,
                objectMapper.getTypeFactory().constructParametricType(CommonResponse.class, BlogVO.class));
        Long blogId = blogResponse.getData().getId();

        // 4. 获取博客列表
        mockMvc.perform(get("/api/blogs")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.list").isArray());

        // 5. 获取博客详情
        mockMvc.perform(get("/api/blogs/" + blogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Integration Test Blog"));

        // 6. 更新博客
        BlogCreateRequest updateReq = new BlogCreateRequest();
        updateReq.setTitle("Updated Integration Test Blog");
        updateReq.setContent("Updated content for integration testing.");

        mockMvc.perform(put("/api/blogs/" + blogId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("Updated Integration Test Blog"));

        // 7. 删除博客
        mockMvc.perform(delete("/api/blogs/" + blogId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getBlogs_withoutAuth_shouldSucceed() throws Exception {
        mockMvc.perform(get("/api/blogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void createBlog_withoutAuth_shouldFail() throws Exception {
        BlogCreateRequest blogReq = new BlogCreateRequest();
        blogReq.setTitle("Test Blog");
        blogReq.setContent("Test content");

        mockMvc.perform(post("/api/blogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blogReq)))
                .andExpect(status().isUnauthorized());
    }
}
