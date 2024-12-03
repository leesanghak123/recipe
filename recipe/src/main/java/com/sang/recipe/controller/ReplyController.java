package com.sang.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sang.recipe.config.auth.PrincipalDetail;
import com.sang.recipe.dto.ReplyWriteDto;
import com.sang.recipe.service.ReplyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reply")
public class ReplyController {
	
	@Autowired
	private ReplyService replyService;
	
	// 댓글 생성
	@PostMapping("/write/{boardId}")
	public ResponseEntity<?> write(@PathVariable int boardId, @Valid @RequestBody ReplyWriteDto replyWriteDto, @AuthenticationPrincipal PrincipalDetail principal) {
		String username = principal.getUsername();
		replyService.글쓰기(boardId, replyWriteDto, username);
		return ResponseEntity.status(HttpStatus.CREATED).body("글 작성 완료");
	}
	
	// 댓글 삭제
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id, @AuthenticationPrincipal PrincipalDetail principal) {
		String username = principal.getUsername();
		replyService.글삭제(id, username);
		return ResponseEntity.status(HttpStatus.OK).body("글 삭제 완료");
	}
}