package com.example.rag.runner;

import com.example.rag.rag.RagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final RagService ragService;

    public ConsoleRunner(RagService ragService) {
        this.ragService = ragService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== RAG Console ===");
        System.out.println("Напиши питання (exit — вихід)");

        while (true) {
            System.out.print("> ");
            String question = scanner.nextLine();

            if ("exit".equalsIgnoreCase(question)) {
                break;
            }

            String answer = ragService.ask(question);
            System.out.println("\n" + answer + "\n");
        }
    }
}
