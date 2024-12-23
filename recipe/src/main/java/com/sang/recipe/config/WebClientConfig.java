// fastapi와 연동하기 위한 WebClient 빈 설정
package com.sang.recipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration // 스프링의 설정 클래스
public class WebClientConfig {

	// WebClient 생성 (@Autowired로 사용가능)
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}