package org.sopt.web1.global.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // application.yml에서 AI 분석 관련 설정 주입
    @Value("${ai.openai.base-url}") // 경로 변경됨: ai.openai.base-url
    private String openaiBaseUrl;

    @Value("${ai.openai.api-key}") // 경로 변경됨: ai.openai.api-key
    private String openaiApiKey;

    /**
     * OpenAI API 호출을 위한 WebClient 설정
     * @return WebClient 인스턴스
     */
    @Bean
    public WebClient openaiWebClient() {
        // OpenAI는 Authorization: Bearer {API_KEY} 헤더를 사용합니다.
        return WebClient.builder()
                .baseUrl(openaiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + openaiApiKey) // API Key를 헤더에 설정
                .build();
    }
}