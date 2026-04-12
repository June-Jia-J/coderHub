package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class AiChatRequest {
    @NotBlank(message = "消息不能为空")
    private String message;
    private java.util.List<MessageItem> history;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public java.util.List<MessageItem> getHistory() { return history; }
    public void setHistory(java.util.List<MessageItem> history) { this.history = history; }

    public static class MessageItem {
        private String role;
        private String content;
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
