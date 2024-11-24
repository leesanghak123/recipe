package com.sang.recipe.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
// 버전 마다 구현 방식이 다름
@Component
public class JWTUtil {

	// 객체 키를 저장할 변수
    private SecretKey secretKey;

    // application.yml에서 들고온 key 값
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

    	// 객체 키 암호화
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // parser를 이용해서 내부 데이터 리턴
    public String getUsername(String token) {

    	// 파싱 / 키를 넣어서 토큰이 우리서버에서 생성되었는지 확인 / 빌더타입으로 리턴 / payload에서 username을 String 타입으로 가져온다
    	return Jwts.parser() // 파싱
        		.verifyWith(secretKey) // 서명을 검증
        		.build() // 파서를 완성하고 사용가능한 상태로 반환
        		.parseSignedClaims(token) // 입력된 JWT 문자열(token)의 내용을 디코딩 후 분석
        		.getPayload()
        		.get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser()
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.get("role", String.class);
    }

    // 만료되었는지 (소멸시 false)
    public Boolean isExpired(String token) {

    	return Jwts.parser()
        		.verifyWith(secretKey)
        		.build()
        		.parseSignedClaims(token)
        		.getPayload()
        		.getExpiration() // payload의 만료시간 필드
        		.before(new Date()); // before : 만료시간이 현재 이후이면 false
    }

    // 로그인 성공 시 토큰생성 메서드
    public String createJwt(String username, String role, Long expiredMs) {

        return Jwts.builder() // jwt 생성
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 발행시간은 현재시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료시간 설정, 설정해주지 않으면 무한
                .signWith(secretKey) // 암호화 진행
                .compact();
    }
}