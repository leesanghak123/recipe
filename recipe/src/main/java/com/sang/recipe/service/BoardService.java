package com.sang.recipe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.model.User;
import com.sang.recipe.dto.BoardDetailDto;
import com.sang.recipe.dto.BoardFindDto;
import com.sang.recipe.dto.BoardWriteDto;
import com.sang.recipe.model.Board;
import com.sang.recipe.model.Likes;
import com.sang.recipe.repository.BoardRepository;
import com.sang.recipe.repository.LikeRepository;
import com.sang.recipe.repository.ReplyRepository;
import com.sang.recipe.repository.UserRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private ReplyRepository replyRepository;
	
	// 값을 넣을 때 DTO와 순서 잘 맞추기
	@Transactional(readOnly = true) // 읽기 전용 트랜잭션
	public Page<BoardFindDto> 글목록(Pageable pageable) {
	    Page<Board> boards = boardRepository.findAll(pageable); // Fetch Join을 통한 조회
	    
	    return boards.map(board -> {
	    
	    
//	    Page<BoardFindDto> boardDtos = boards.map(board -> { // boards를 Dto로 변형
//	        // 댓글 수 계산: 댓글 + 대댓글
//	        int replyCount = board.getReply().stream() // stream: 리스트를 스트림으로 변환(각각의 댓글에 대한 연산 수행 가능)
//	            .mapToInt(reply -> 1 + (reply.getReplyreply() != null ? reply.getReplyreply().size() : 0)) // 댓글과 대댓글 수
//	            .sum();

	        return new BoardFindDto(
	            board.getId(),
	            board.getTitle(),
	            board.getUser().getUsername(), // User에서 username만 가져옴
	            board.getCreateDate(),
	            board.getReplyCnt(), // 댓글 수와 대댓글 수 합산
	            board.getCount(),
	            board.getLikes()
	        );
	    });
	    //return boardDtos;
	}

	
	@Transactional
	public BoardDetailDto 글상세보기(int id, String username) {
	    // 1. ID로 게시물 조회
		Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

	    // 조회수 증가
	    board.setCount(board.getCount() + 1); // 더티채킹
	  
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
	    
	    // 좋아요 여부 확인
	    boolean isLiked = likeRepository.existsByUserAndBoard(user, board);
	    
	    // 3. Board를 BoardDetailDto로 변환 및 반환
	    return new BoardDetailDto(
	            board.getId(),
	            board.getTitle(),
	            board.getContent(),
	            board.getUser().getUsername(),
	            board.getCreateDate(),
	            board.getReply(),
	            board.getLikes(),
	            isLiked // 좋아요 여부 추가
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
    
    
    @Transactional
    public boolean 추천여부(int boardId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 좋아요 여부 확인 (객체를 받기 때문에 Boolean이 아니라 Optional)
        Optional<Likes> existingLike = likeRepository.findByUserAndBoard(user, board);

        try {
	        if (existingLike.isPresent()) { // isPresent: Optional 클래스에서 제공하는 메서드로 객체의 값이 존재하는지 확인
	            // 좋아요 취소
	            likeRepository.delete(existingLike.get());
	            //board.setLikes(board.getLikes() - 1);
	            boardRepository.decrementLikes(boardId); // 좋아요 수 감소 (원자적_UPDATE)
	            
	            //return false; // 좋아요 취소
	        } else {
	            // 좋아요 추가
	            Likes like = new Likes();
	            like.setUser(user);
	            like.setBoard(board);
	            likeRepository.save(like);
	            //board.setLikes(board.getLikes() + 1);
	            boardRepository.incrementLikes(boardId); // 좋아요 수 증가 (원자적_UPDATE)
	            
	            //return true; // 좋아요 추가
	        }
	        boardRepository.save(board);
	        return !existingLike.isPresent();
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalStateException("동시에 다른 사용자가 데이터를 수정했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    @Transactional
    public int 좋아요수(int boardId) {
        return likeRepository.findLikesCountByBoardId(boardId);
    }
}