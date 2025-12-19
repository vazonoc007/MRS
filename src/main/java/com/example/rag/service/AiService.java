package com.example.rag.service;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final OllamaChatModel chatModel;

    public AiService(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String ask(String prompt) {
        return chatModel.call(prompt);
    }
}
