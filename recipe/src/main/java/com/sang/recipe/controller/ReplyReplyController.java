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
import com.sang.recipe.dto.ReplyReplyWriteDto;
import com.sang.recipe.service.ReplyReplyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/replyreply")
public class ReplyReplyController {
	
	@Autowired
	private ReplyReplyService replyReplyService;
	
	// 대댓글 생성
	@PostMapping("/write/{replyId}")
	public ResponseEntity<?> write(@PathVariable int replyId, @Valid @RequestBody ReplyReplyWriteDto replyReplyWriteDto, @AuthenticationPrincipal PrincipalDetail principal) {
		String username = principal.getUsername();
		replyReplyService.글쓰기(replyId, replyReplyWriteDto, username);
		return ResponseEntity.status(HttpStatus.OK).body("대댓글 생성 완료");
		
	}
	
	// 대댓글 삭제
	@DeleteMapping("/delete/{replyReplyId}")
	public ResponseEntity<?> delete(@PathVariable int replyReplyId, @AuthenticationPrincipal PrincipalDetail principal) {
		String username = principal.getUsername();
		replyReplyService.글삭제(replyReplyId, username);
		return ResponseEntity.status(HttpStatus.OK).body("대댓글 삭제 완료");
	}
}
