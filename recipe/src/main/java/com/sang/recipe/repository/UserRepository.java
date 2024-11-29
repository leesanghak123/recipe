package com.sang.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sang.recipe.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    // username으로 User 조회 (Optional 사용)
	// Optional로 하면 null값이 나오는 경우 안전하게 처리가능
    Optional<User> findByUsername(String username);
    
    // 회원가입 시 회원이 있나 없나 검증하기 위한 메서드
    Boolean existsByUsername(String username);
}