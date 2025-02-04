package com.sang.recipe.repository;

import java.util.Optional;

import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sang.recipe.model.Board;

import jakarta.transaction.Transactional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    // Pagination: EntityGraph를 사용하여 필요한 필드를 Fetch Join
    //@EntityGraph(attributePaths = {"user", "reply", "reply.replyreply"})
    //@BatchSize(size=10)
    //Page<Board> findAll(Pageable pageable);
    
	@EntityGraph(attributePaths = {"user"})
    Page<Board> findAll(Pageable pageable);
    

    // 글 상세보기: EntityGraph를 사용하여 필요한 필드를 Fetch Join
    @EntityGraph(attributePaths = {"user", "reply", "reply.replyreply"})
    Optional<Board> findById(int boardId);
    
    // 좋아요 수 증가: 원자적 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.likes = b.likes + 1 WHERE b.id = :boardId")
    int incrementLikes(@Param("boardId") int boardId);

    // 좋아요 수 감소: 원자적 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Board b SET b.likes = b.likes - 1 WHERE b.id = :boardId")
    int decrementLikes(@Param("boardId") int boardId);
}