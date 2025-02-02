package com.sang.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sang.recipe.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer>{
	//@EntityGraph(attributePaths = {"replyreply"})
	//List<Reply> findByBoardId(int boardId);
	
	//@Query("SELECT COUNT(r) + COALESCE(SUM(SIZE(r.replyreply)), 0) FROM Reply r WHERE r.board.id = :boardId")
	//int countTotalRepliesByBoardId(@Param("boardId") int boardId);

}