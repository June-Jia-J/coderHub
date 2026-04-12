package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BlogCreateRequest {

    @NotBlank(message = "标题不能为空")
    @Size(max = 256, message = "标题最长 256 字")
    private String title;

    @NotBlank(message = "正文不能为空")
    private String content;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
