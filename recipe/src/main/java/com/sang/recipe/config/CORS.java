package com.sang.recipe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.Arrays;

@Configuration
public class CORS {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8003")); // 허용할 도메인 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // 허용할 헤더
        configuration.setAllowCredentials(true); // 자격 증명(예: 쿠키, 인증 헤더) 허용

        // exposeHeaders를 추가하여 클라이언트가 Authorization 헤더를 읽을 수 있도록 설정
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // 응답 헤더 중 Authorization 노출
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용
        return source;
    }
}