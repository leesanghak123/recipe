package com.sang.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.model.Board;
import com.sang.recipe.repository.BoardRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	
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
}