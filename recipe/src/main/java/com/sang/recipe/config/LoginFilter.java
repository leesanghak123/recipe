package com.sang.recipe.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sang.recipe.config.auth.PrincipalDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

	// autowired로 받으니까 SecurityConfig에서 에러발생 (필터 등록 시점에는 @autowired 주입이 제대로 작동하지 않을
	// 수 있다.)
	private final AuthenticationManager authenticationManager;

	//JWTUtil 주입
	private final JWTUtil jwtUtil;
	
	// 생성자
	public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil; // 시큐리티 컴피그에 필터를 등록해놨기 때문에 시큐리티 컴피그도 수정해야 오류가 사라진다
	}

	// 리턴타입은 인증을 하는 Authentication
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
		// 클라이언트 요청에서 username, password 추출
		// JSON 형식의 데이터를 읽고 파싱
        ObjectMapper objectMapper = new ObjectMapper(); // JSON 데이터 읽기
        Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);

        // JSON에서 username과 password 추출
        String username = loginData.get("username");
        String password = loginData.get("password");
		
		//System.out.println("Login attempt for username: " + username);
        //System.out.println("Password provided: " + password);
		
		// 매니저에게 던져주기
		// 던져주기 위해서는 Dto와 같이 담아서 줘야한다
		// 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함 (아이디, 비밀번호, 권한)
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

		// token에 담은 검증을 위한 AuthenticationManager로 전달
		return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
	        throw new AuthenticationServiceException("Error parsing login request");
	    }
	}

	// 인증 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
	    
	    PrincipalDetail customUserDetails = (PrincipalDetail) authentication.getPrincipal();

	    // authentication 객체로 부터 username 뽑아내기
	    String username = customUserDetails.getUsername();

	    // role 값 뽑아내기
	    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
	    GrantedAuthority auth = iterator.next();

	    String role = auth.getAuthority().replace("Role_", ""); // "Role_" 접두사를 제거합니다.
	    System.out.println("Role = " + role);

	    // jwtUtil에 토큰을 만들어 달라고 던져주는 부분
	    // createJwt : jwt 생성 메서드 (10시간(60초 * 60 * 10 * 1000), L은 long 자료형을 의미)
	    String token = jwtUtil.createJwt(username, role, 60 * 60 * 10L * 1000);

	    // 콘솔에 JWT 토큰 출력
	    System.out.println("Generated JWT Token: " + token);

	    // Header부분에 담아서 클라이언트에게 응답, key : Authorization
	    response.addHeader("Authorization", "Bearer " + token);
	}


	// 인증 실패시 실행하는 메소드
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
		response.setStatus(401);
	}
}