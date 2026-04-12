package com.example.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaConfig {

    @Value("${ollama.base-url:http://host.docker.internal:11434}")
    private String baseUrl;

    @Value("${ollama.model:deepseek-r1:1.5b}")
    private String model;

    @Bean
    public String ollamaBaseUrl() {
        return baseUrl;
    }

    @Bean
    public String ollamaModel() {
        return model;
    }
}
