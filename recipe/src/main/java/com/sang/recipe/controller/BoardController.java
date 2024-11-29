package com.sang.recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.dto.BoardWriteDto;
import com.sang.recipe.model.Board;
import com.sang.recipe.service.BoardService;
import com.sang.recipe.config.JWTUtil;
import com.sang.recipe.config.auth.PrincipalDetail;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boards") // 공통 경로 설정
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JWTUtil jwtUtil;

    // 글 목록 조회
    @GetMapping
    public ResponseEntity<Page<BoardFindDto>> findPage(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardFindDto> boards = boardService.글목록(pageable);
        return ResponseEntity.ok(boards);
    }

    // 글 작성
    @PostMapping("/write")
    // @Valid: Dto에서 유효성 검사가 되었는지 확인, 아니면 요청 거부, @AuthenticationPrincipal: SecurityContext에서 인증 객체 정보를 자동으로 가져온다
    public ResponseEntity<?> write(@Valid @RequestBody BoardWriteDto boardWriteDto, @AuthenticationPrincipal PrincipalDetail principal) {
        // username을 SecurityContext에서 인증 객체 가져오기
        String username = principal.getUsername();

        boardService.글쓰기(boardWriteDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body("글 작성 완료");
    }
}
