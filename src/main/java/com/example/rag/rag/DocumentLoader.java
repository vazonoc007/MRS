package com.example.rag.rag;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentLoader {

    public List<String> loadDocuments() {
        List<String> documents = new ArrayList<>();

        try {
            PathMatchingResourcePatternResolver resolver =
                    new PathMatchingResourcePatternResolver();

            Resource[] resources = resolver.getResources("classpath:docs/*.txt");

            for (Resource resource : resources) {
                String content = Files.readString(resource.getFile().toPath());
                documents.add(content);
            }

        } catch (Exception e) {
            throw new RuntimeException("Помилка завантаження документів", e);
        }

        return documents;
    }
}
