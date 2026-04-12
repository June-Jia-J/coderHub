package com.example.blog.controller;

import com.example.blog.dto.*;
import com.example.blog.service.OllamaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final OllamaService ollamaService;

    public AiController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/chat")
    public CommonResponse<AiChatResponse> chat(
            @Valid @RequestBody AiChatRequest req,
            HttpServletRequest request) {
        String reply = ollamaService.chat(req.getMessage(), req.getHistory());
        return CommonResponse.ok(new AiChatResponse(reply));
    }

    @PostMapping("/generate")
    public CommonResponse<AiGenerateResponse> generate(
            @Valid @RequestBody AiGenerateRequest req,
            HttpServletRequest request) {
        String content = ollamaService.generate(
                req != null ? req.getTitle() : null,
                req != null ? req.getOutline() : null);
        return CommonResponse.ok(new AiGenerateResponse(content));
    }
}
