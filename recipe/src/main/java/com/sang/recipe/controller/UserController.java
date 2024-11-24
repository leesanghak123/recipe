package com.sang.recipe.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sang.recipe.dto.UserJoinDto;
import com.sang.recipe.model.User;
import com.sang.recipe.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
    
	
	//@CrossOrigin(origins = "http://localhost:8003")
	@PostMapping("/auth/join")
	// @RequestBody : JSON 데이터를 Dto로 변환
	public ResponseEntity<String> join(@Valid @RequestBody UserJoinDto userJoinDto) {
	    userService.회원가입(userJoinDto);
	    return ResponseEntity.ok("회원가입 성공");
	}
}