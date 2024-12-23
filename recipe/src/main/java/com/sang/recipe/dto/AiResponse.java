package com.sang.recipe.dto;

import java.sql.Timestamp;
import java.util.List;

import com.sang.recipe.model.Reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiResponse {
	private String cookingMethod; // 방법
	private String cookingCategory; // 방식
	private String ingredients; // 재료
	private String content; // 응답
}