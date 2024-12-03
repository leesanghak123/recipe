package com.sang.recipe.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.dto.ReplyWriteDto;
import com.sang.recipe.model.Board;
import com.sang.recipe.model.Reply;
import com.sang.recipe.model.User;
import com.sang.recipe.repository.BoardRepository;
import com.sang.recipe.repository.ReplyRepository;
import com.sang.recipe.repository.UserRepository;

@Service
public class ReplyService {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Transactional
	public void 글쓰기(int boardId, ReplyWriteDto replyWriteDto, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
		
		Board board = boardRepository.findById(boardId)
				.orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardId));
			
		Reply reply = new Reply();
		reply.setContent(replyWriteDto.getContent());
		reply.setUser(user);
		reply.setBoard(board);
		reply.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
		
		replyRepository.save(reply);
	}
	
	@Transactional
	public void 글삭제(int replyId, String username) {
		Reply reply = replyRepository.findById(replyId)
				.orElseThrow(() -> new IllegalArgumentException("Reply not found: " + replyId));
		
		if (!reply.getUser().getUsername().equals(username)) {
			throw new IllegalArgumentException("본인의 글만 삭제할 수 있습니다.");
		}
		
		replyRepository.delete(reply);
	}
}
