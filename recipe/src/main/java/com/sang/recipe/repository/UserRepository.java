package com.sang.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sang.recipe.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	// Optional<User> findByUsername(String username);
	// 특정 유저 조회 (PrincipalDetailService에서 쓰임)
	User findByUsername(String username);
	
	// 회원가입 시 회원이 있나 없나 검증하기 위함 (existsByUsername는 정해져있는 메서드)
	Boolean existsByUsername(String username);
}