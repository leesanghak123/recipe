package com.sang.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.model.User;
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
	
	@Transactional(readOnly = true)
	public Page<BoardFindDto> 글목록(Pageable pageable){
		Page<Board> boards = boardRepository.findAll(pageable);
		// board는 boards에서 나온 각각의 객체
		Page<BoardFindDto> boardDtos = boards.map(board -> new BoardFindDto(
				board.getId(),
				board.getTitle(),
				board.getContent(),
				board.getCount(),
				board.getLikes(),
				board.getUser().getUsername(), // User에서 username만 가져옴
				board.getCreateDate()
		));
		return boardDtos;
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

}