package com.example.blog.dto;

import jakarta.validation.constraints.Size;

public class AiGenerateRequest {
    @Size(max = 200, message = "标题长度不能超过200")
    private String title;
    @Size(max = 2000, message = "大纲长度不能超过2000")
    private String outline;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getOutline() { return outline; }
    public void setOutline(String outline) { this.outline = outline; }
}
