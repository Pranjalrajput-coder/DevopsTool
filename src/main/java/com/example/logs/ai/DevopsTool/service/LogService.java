package com.example.logs.ai.DevopsTool.service;

import com.example.logs.ai.DevopsTool.entity.LogEntity;
import com.example.logs.ai.DevopsTool.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;


    public String analyzeLog(String log) {

        float[] embedding = embeddingModel.embed(log);

        // For find Similar Logs from Database, we are using embedding
        var similarLogs = vectorStore.similaritySearch(SearchRequest.builder()
                        .query(log)
                        .topK(3)
                        .similarityThreshold(0.2)
                .build());

        String context = similarLogs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));


        // For find similar logs from Repo operations.
//        var checkFromRepo = logRepository.findSimilarLogs(embedding);

//        String context = checkFromRepo.stream()
//                .map(LogEntity::getLogSuggestion)
//                .collect(Collectors.joining("\n"));

        // Prompt engineering
        String prompt = """
        You are a senior DevOps engineer.

        Use the following past logs as reference:
        %s
    
        Now analyze this log:
        %s
    
        Give:
        - Issue
        - Explanation
        - Fix
        """.formatted(context, log);

        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        String concisedContext = promptTemplate.render(Map.of("context", context));

        String output = chatClient.prompt()
                .user(concisedContext)
                .advisors(
                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
                                .defaultTopK(3)
                                .build()
                )
                .call()
                .content();

        // Save log
        saveData(log,embedding,output);

        return output;
    }

    private void saveData(String log, float[] embedding, String output){

                LogEntity entity = new LogEntity();
                entity.setContent(log);
                entity.setCreatedAt(LocalDateTime.now());
                entity.setEmbedding(embedding);
                entity.setLogSuggestion(output);

                logRepository.save(entity);
    }
}
