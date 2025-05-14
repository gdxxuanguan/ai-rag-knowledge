package com.xuanguan.dev.config;


import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeepSeekConfig {

    @Value("${spring.ai.deepseek.api.key}")
    private String apiKey;

    @Bean
    public OpenAiApi.ChatModel deepSeekChatModel() {
        // 创建自定义ChatModel实现
        return new DeepSeekChatModel(apiKey);
    }

    @Bean
    public ChatClient deepSeekChatClient(OpenAiApi.ChatModel deepSeekChatModel) {
        return ChatClient.builder(deepSeekChatModel)
                .defaultSystem("你是一个人工智能助手")
                .build();
    }
}