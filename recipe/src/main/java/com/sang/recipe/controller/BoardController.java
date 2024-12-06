package com.sang.recipe.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.sang.recipe.dto.BoardDetailDto;
import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.dto.BoardWriteDto;
import com.sang.recipe.model.Board;
import com.sang.recipe.service.BoardService;
import com.sang.recipe.config.auth.PrincipalDetail;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/boards") // 공통 경로 설정
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 글 목록 조회
    @GetMapping
    public ResponseEntity<Page<BoardFindDto>> findPage(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardFindDto> boards = boardService.글목록(pageable);
        return ResponseEntity.ok(boards);
    }
    
    // 글 상세보기
    @GetMapping("/{id}")
    public ResponseEntity<?> Detali(@PathVariable int id, @AuthenticationPrincipal PrincipalDetail principal) {
    	try {
    		String username = principal.getUsername();
            // Service 계층에서 ID로 게시글 상세 정보 조회
            BoardDetailDto boardDetail = boardService.글상세보기(id, username);

            // 게시글이 존재하지 않을 경우
            if (boardDetail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 게시글을 찾을 수 없습니다.");
            }

            // 성공 시 게시글 상세 정보를 반환
            return ResponseEntity.ok(boardDetail);
        } catch (Exception e) {
            // 예외 발생 시 내부 서버 에러 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 상세 정보를 가져오는 중 문제가 발생했습니다.");
        }	
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
    
    // 글 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody BoardWriteDto boardWriteDto, @AuthenticationPrincipal PrincipalDetail principal) {
        try {
            String username = principal.getUsername();

            // 게시글 수정 처리
            boardService.글수정(id, boardWriteDto, username);

            return ResponseEntity.status(HttpStatus.OK).body("글 수정 완료");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 수정 중 오류가 발생했습니다.");
        }
    }
    
    // 글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @AuthenticationPrincipal PrincipalDetail principal) {
        try {
            String username = principal.getUsername();

            boardService.글삭제(id, username);

            return ResponseEntity.status(HttpStatus.OK).body("글 삭제 완료");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 삭제 중 오류가 발생했습니다.");
        }
    }
    
    // 추천수 처리
    @PostMapping("/{id}/like")
    public ResponseEntity<?> handleLike(@PathVariable int id, @AuthenticationPrincipal PrincipalDetail principal) {
        try {
            String username = principal.getUsername();

            // 좋아요 상태 변경 처리
            boolean isLiked = boardService.추천여부(id, username);
            
            int currentLikes = boardService.좋아요수(id);

            // 응답 데이터 설정
            Map<String, Object> response = new HashMap<>();
            response.put("isLiked", isLiked);
            response.put("likes", currentLikes);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // 서버 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("추천수 처리 중 오류가 발생했습니다.");
        }
    }
}