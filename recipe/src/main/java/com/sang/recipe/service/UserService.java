package com.sang.recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sang.recipe.dto.UserJoinDto;
import com.sang.recipe.model.RoleType;
import com.sang.recipe.model.User;
import com.sang.recipe.repository.UserRepository;

@Service
public class UserService {	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private BCryptPasswordEncoder encoder;
	
	@Transactional
    public void 회원가입(UserJoinDto userJoinDto) {
		String username = userJoinDto.getUsername();
		String rawPassword = userJoinDto.getPassword(); // 원문 비밀번호
        String encPassword = encoder.encode(rawPassword); // 해시
        
        // 회원가입 하기 전 값이 있나 없나를 boolean 반환값으로 확인
        Boolean isExist = userRepository.existsByUsername(username);
        
        // user가 있다면 예외 발생, throw: 에러를 강제적으로 호출자에게 알림, return: 값으로 알림
        if(isExist) {
        	throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }
        
		User user = User.builder()
                .username(userJoinDto.getUsername())
                .password(encPassword)
                .email(userJoinDto.getEmail())
                .build();
		
		user.setRole(RoleType.ROLE_User);
		
		userRepository.save(user);
    }
}
