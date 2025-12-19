package com.example.rag.rag;

import com.example.rag.service.AiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final AiService aiService;
    private final List<String> documents;

    public RagService(AiService aiService, DocumentLoader loader) {
        this.aiService = aiService;
        this.documents = loader.loadDocuments();
    }

    public String ask(String question) {

        String context = String.join("\n---\n", documents);

        String prompt = """
                Відповідай на питання, використовуючи інформацію з контексту нижче.
                Якщо відповіді немає — скажи, що інформація відсутня.

                КОНТЕКСТ:
                %s

                ПИТАННЯ:
                %s
                """.formatted(context, question);

        return aiService.ask(prompt);
    }
}
