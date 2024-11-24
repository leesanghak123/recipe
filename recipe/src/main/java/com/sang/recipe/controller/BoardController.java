package com.sang.recipe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.service.BoardService;

@RestController // REST API
public class BoardController {
	@Autowired
	private BoardService boardService;

	//@CrossOrigin(origins = "http://localhost:8003")
	// @PageableDefault 설정 후 Pageable을 통해 요청
	@GetMapping("/api/boards")
	public ResponseEntity<Page<BoardFindDto>> findPage(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<BoardFindDto> boards = boardService.글목록(pageable); // Page<BoardFindDto> 타입으로 반환
		return ResponseEntity.ok(boards); // 200OK와 함께 응답 본문 반환
	}
}