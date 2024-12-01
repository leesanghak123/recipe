package com.sang.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.model.User;
import com.sang.recipe.dto.BoardDetailDto;
import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.dto.BoardWriteDto;
import com.sang.recipe.model.Board;
import com.sang.recipe.repository.BoardRepository;
import com.sang.recipe.repository.UserRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
    private UserRepository userRepository;
	
	// 값을 넣을 때 DTO와 순서 잘 맞추기
	@Transactional(readOnly = true)
	public Page<BoardFindDto> 글목록(Pageable pageable){
		Page<Board> boards = boardRepository.findAll(pageable);
		// board는 boards에서 나온 각각의 객체
		Page<BoardFindDto> boardDtos = boards.map(board -> new BoardFindDto(
				board.getId(),
				board.getTitle(),
				board.getUser().getUsername(), // User에서 username만 가져옴
				board.getCreateDate(),
				board.getReply() != null ? board.getReply().size() : 0, // 댓글 수 계산
				board.getCount(),
				board.getLikes()
		));
		return boardDtos;
	}
	
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션
	public BoardDetailDto 글상세보기(int id) {
	    // 1. ID로 게시물 조회
	    Board board = boardRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

	    // 2. Board를 BoardDetailDto로 변환 및 반환
	    return new BoardDetailDto(
	            board.getId(),
	            board.getTitle(),
	            board.getContent(),
	            board.getUser().getUsername(),
	            board.getCreateDate(),
	            board.getReply(),
	            board.getCount(),
	            board.getLikes()
	    );
	}

	
	@Transactional
	public void 글쓰기(BoardWriteDto boardWriteDto, String username) { 
	    // 1. 사용자 조회
	    User user = userRepository.findByUsername(username)
	    		.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

	    // 2. Board 엔티티 생성 및 DTO 값 설정
        Board board = new Board();
        board.setTitle(boardWriteDto.getTitle());
        board.setContent(boardWriteDto.getContent());
        board.setCount(0); // 조회수 초기화
        board.setLikes(0);  // 좋아요 수 초기화
        board.setUser(user); // 연관 사용자 설정

        // 3. 게시글 저장
        boardRepository.save(board);
	}
	

    @Transactional
    public void 글수정(int id, BoardWriteDto boardWriteDto, String username) {
        // 게시글 조회
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 게시글 작성자와 현재 로그인한 사용자가 일치하는지 확인
        if (!board.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인의 글만 수정할 수 있습니다.");
        }

        // 게시글 수정 처리
        board.setTitle(boardWriteDto.getTitle());
        board.setContent(boardWriteDto.getContent());

        // 수정된 게시글 저장
        boardRepository.save(board);
    }
    
    
    @Transactional
    public void 글삭제(int id, String username) {
        // 게시글 조회
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 게시글 작성자와 현재 로그인한 사용자가 일치하는지 확인
        if (!board.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인의 글만 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        boardRepository.delete(board);
    }
    

}