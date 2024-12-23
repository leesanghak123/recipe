package com.sang.recipe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sang.recipe.dto.AiRequest;
import com.sang.recipe.dto.AiResponse;

import reactor.core.publisher.Mono;

@Service
public class AiService {
	// 외부 API와 HTTP 요청을 통해 통신하는 클라이언트, 클래스 내부에서만 접근 가능
	private final WebClient webClient;

	// WebClient.Builder(Bean으로 등록되어 있음)를 webClient에 할당
	// WebClient.Builder는 메뉴판, webClient는 메뉴라고 생각하면 된다
    @Autowired
    public AiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build();  // FastAPI 서버 주소
    }

    public AiResponse ai요리(AiRequest request) {
        // FastAPI로 요청 보내기
    	// Mono: 비동기 처리
    	Mono<String> fastApiResponse = this.webClient.post()
                .uri("/api/question") // fastapi 질문 하는 api
                .bodyValue(request) // requestDto
                .retrieve() // 200인 경우 반환, 아닌 경우 예외처리
                .bodyToMono(String.class); // 응답 데이터 String으로 변환

    	// FastAPI에서 받은 String 값을 이용하여 AiResponse 객체 생성
        String responseContent = fastApiResponse.block(); // 비동기 처리를 동기적으로 대기(값을 받을 때까지 대기)
    	
        // 로그 찍기 (응답 값 확인)
        if (responseContent == null) {
            System.out.println("FastAPI response is null");
        } else {
            System.out.println("FastAPI response content: " + responseContent);
        }
        
        // 요청 받은 데이터와 함께 응답을 결합하여 AiResponse 생성
        return new AiResponse(
            request.getCookingMethod(), // request에서 값 가져오기
            request.getCookingCategory(),
            request.getIngredients(),
            responseContent != null ? responseContent : "응답 내용이 없습니다."
        );
    }
}