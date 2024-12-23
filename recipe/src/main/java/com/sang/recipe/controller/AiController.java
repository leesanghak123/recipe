package com.sang.recipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sang.recipe.config.auth.PrincipalDetail;
import com.sang.recipe.dto.AiRequest;
import com.sang.recipe.dto.AiResponse;
import com.sang.recipe.dto.BoardWriteDto;
import com.sang.recipe.service.AiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ai")
public class AiController {
	
	@Autowired
	private AiService aiService;
	
	@PostMapping("/service")
	// @Valid: Dto에서 유효성 검사가 되었는지 확인, 아니면 요청 거부
    public ResponseEntity<?> recipe(@Valid @RequestBody AiRequest request) {
		try {
			AiResponse recipes = aiService.ai요리(request);
			
			// 출력 결과 보기
            System.out.println("AiResponse content: " + recipes.getContent());
			
            return ResponseEntity.ok().body(recipes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while filtering recipes.");
        }
    }
}