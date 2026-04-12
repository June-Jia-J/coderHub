package com.example.blog.service;

import com.example.blog.dto.AiChatRequest;
import com.example.blog.dto.AiGenerateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaService {

    private static final Logger log = LoggerFactory.getLogger(OllamaService.class);
    private static final int TIMEOUT_MS = 60000;

    private final String baseUrl;
    private final String model;
    private final RestTemplate restTemplate;

    public OllamaService(
            @Qualifier("ollamaBaseUrl") String baseUrl,
            @Qualifier("ollamaModel") String model) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.restTemplate = new RestTemplate();
    }

    public String chat(String message, List<AiChatRequest.MessageItem> history) {
        List<Map<String, String>> messages = new ArrayList<>();
        if (history != null) {
            for (AiChatRequest.MessageItem item : history) {
                Map<String, String> m = new HashMap<>();
                m.put("role", "user".equalsIgnoreCase(item.getRole()) ? "user" : "assistant");
                m.put("content", item.getContent());
                messages.add(m);
            }
        }
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        messages.add(userMsg);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", false);

        try {
            String url = baseUrl + "/api/chat";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (resp.getBody() != null && resp.getBody().get("message") != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> msg = (Map<String, Object>) resp.getBody().get("message");
                Object content = msg.get("content");
                return content != null ? content.toString() : "";
            }
            return "";
        } catch (Exception e) {
            log.error("Ollama chat 调用失败: {}", e.getMessage());
            throw new com.example.blog.exception.BusinessException(503, "AI 服务暂不可用，请确认 Ollama 已启动且模型已拉取");
        }
    }

    public String generate(String title, String outline) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请根据以下标题写一篇技术博客正文，使用 Markdown 格式。\n\n");
        prompt.append("标题：").append(title != null ? title : "未命名").append("\n\n");
        if (outline != null && !outline.isBlank()) {
            prompt.append("提纲或要求：\n").append(outline).append("\n\n");
        }
        prompt.append("直接输出博客正文内容，不要重复标题。");

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt.toString());
        body.put("stream", false);

        try {
            String url = baseUrl + "/api/generate";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (resp.getBody() != null && resp.getBody().get("response") != null) {
                return resp.getBody().get("response").toString();
            }
            return "";
        } catch (Exception e) {
            log.error("Ollama generate 调用失败: {}", e.getMessage());
            throw new com.example.blog.exception.BusinessException(503, "AI 服务暂不可用，请确认 Ollama 已启动且模型已拉取");
        }
    }
}
