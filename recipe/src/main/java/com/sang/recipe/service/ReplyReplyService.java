package com.sang.recipe.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.dto.ReplyReplyWriteDto;
import com.sang.recipe.model.Reply;
import com.sang.recipe.model.ReplyReply;
import com.sang.recipe.model.User;
import com.sang.recipe.repository.ReplyReplyRepository;
import com.sang.recipe.repository.ReplyRepository;
import com.sang.recipe.repository.UserRepository;

@Service
public class ReplyReplyService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private ReplyReplyRepository replyReplyRepository;
	
	@Transactional
	public void 글쓰기(int replyId, ReplyReplyWriteDto replyReplyWriteDto, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
		
		Reply reply = replyRepository.findById(replyId)
				.orElseThrow(() -> new IllegalArgumentException("Board not found: " + replyId));
		
		ReplyReply replyreply = new ReplyReply();
		replyreply.setContent(replyReplyWriteDto.getContent());
		replyreply.setUser(user);
		replyreply.setReply(reply);
		replyreply.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

		replyReplyRepository.save(replyreply);
	}
	
	@Transactional
	public void 글삭제(int replyReplyId, String username) {
		ReplyReply replyreply = replyReplyRepository.findById(replyReplyId)
				.orElseThrow(() -> new IllegalArgumentException("Reply not found: " + replyReplyId));
		
		if (!replyreply.getUser().getUsername().equals(username)) {
			throw new IllegalArgumentException("본인의 글만 삭제할 수 있습니다.");
		}
		
		replyReplyRepository.delete(replyreply);
	}
}
