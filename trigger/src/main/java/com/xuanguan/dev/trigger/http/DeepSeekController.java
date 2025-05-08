package com.xuanguan.dev.trigger.http;

import com.xuanguan.dev.api.response.Response;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/deepseek/")
public class DeepSeekController {

    @Resource
    private PgVectorStore pgVectorStore;

    @Value("${spring.ai.deepseek.api.key}")
    private String apiKey;



    @PostMapping("chat")
    public Mono<Response<String>> chatDeepseek(
            @RequestParam List<String> ragTagList,
            @RequestParam String message) {

        // 1. 构建向量数据库查询条件
        String filter = ragTagList.stream()
                .map(tag -> "knowledge == '" + tag + "'")
                .collect(Collectors.joining(" OR "));

        SearchRequest searchRequest = SearchRequest.query(message)
                .withTopK(5)
                .withFilterExpression(filter);

        // 2. 执行向量搜索
        List<Document> documents = pgVectorStore.similaritySearch(searchRequest);
        String context = documents.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        // 3. 动态生成系统提示
        String systemPrompt = MessageFormat.format("""
    Use the information from the DOCUMENTS section to provide accurate answers.
    If unsure, state you dont know. Reply in Chinese!
    DOCUMENTS: {0}
    """, context);

        // 4. 构建API请求消息体
        List<Map<String, String>> apiMessages = new ArrayList<>();
        apiMessages.add(Map.of(
                "role", "system",
                "content", systemPrompt
        ));
        apiMessages.add(Map.of(
                "role", "user",
                "content", message
        ));

        // 5. 配置WebClient调用
        return WebClient.builder()
                .baseUrl("https://api.deepseek.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build()
                .post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "model", "deepseek-chat",
                        "messages", apiMessages,
                        "stream", false
                ))
                .retrieve()
                .bodyToMono(DeepSeekResponse.class)
                .map(response -> {
                    if (!response.getChoices().isEmpty()) {
                        return Response.<String>builder().code("0000").info("success").data(response.getChoices().get(0).getMessage().getContent()).build();
                    }
                    return Response.<String>builder().code("1111").info("fail").build();
                });
    }

    // 6. 定义响应DTO
    @Data
    static class DeepSeekResponse {
        private List<Choice> choices;

        @Data
        static class Choice {
            private Message message;
        }

        @Data
        static class Message {
            private String role;
            private String content;
        }
    }
}
