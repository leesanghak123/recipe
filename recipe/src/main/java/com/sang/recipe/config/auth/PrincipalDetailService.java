package com.sang.recipe.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sang.recipe.config.JWTUtil;
import com.sang.recipe.model.User;
import com.sang.recipe.repository.UserRepository;

@Service // Bean 등록
public class PrincipalDetailService implements UserDetailsService{
	
	private final UserRepository userRepository;

	public PrincipalDetailService(UserRepository userRepository) {
		
		this.userRepository = userRepository;
	}
	
	// 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
	// password 부분 처리는 알아서 한다
	// 우리는 username이 DB에 있는지만 확인해주면 됨 (loadUserByUsername)
	// 이걸 override해서 안만들어주면 PrincipalDetail에서 생성자를 못만들어줘서
	// password가 콘솔창으로 설정된다
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Optional을 사용하여 사용자 정보 가져오기
        User principal = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + username));

        return new PrincipalDetail(principal);  // PrincipalDetail로 반환
    }
}