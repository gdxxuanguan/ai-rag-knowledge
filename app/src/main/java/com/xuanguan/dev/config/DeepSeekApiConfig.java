package com.xuanguan.dev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DeepSeekApiConfig {

    @Value("${spring.ai.deepseek.api.base-url}")
    private String baseUrl;

    @Bean
    public WebClient deepSeekWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}