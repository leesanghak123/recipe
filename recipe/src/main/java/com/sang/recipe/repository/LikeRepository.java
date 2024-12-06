package com.sang.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sang.recipe.model.Board;
import com.sang.recipe.model.Likes;
import com.sang.recipe.model.User;

public interface LikeRepository extends JpaRepository<Likes, Integer>{
	
	// 추천 여부를 파악해서 추천 수를 고치기 위함
	Optional<Likes> findByUserAndBoard(User user, Board board);
	
	// 단순 추천 여부 파악(Detail)
	boolean existsByUserAndBoard(User user, Board board);
	
	// 추천 개수 계산, l: Likes를 참조하는 변수
	@Query("SELECT COUNT(l) FROM Likes l WHERE l.board.id = :boardId")
	int findLikesCountByBoardId(@Param("boardId") int boardId);
}