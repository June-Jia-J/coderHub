package com.example.blog.dto;

public class AiGenerateResponse {
    private String content;

    public AiGenerateResponse(String content) {
        this.content = content;
    }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
