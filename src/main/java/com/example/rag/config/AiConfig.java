package com.example.rag.config;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public OllamaChatModel ollamaChatModel() {
        return new OllamaChatModel("http://localhost:11434", "llama3");
    }
}
