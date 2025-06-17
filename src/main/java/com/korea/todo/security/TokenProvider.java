package com.korea.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.korea.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	
	//비밀키
	private static final String SECRET_KEY = "edc043eae3474e6f151dd52f97b2177173a156db469b281388559873acc62562b7fd1dab28aa33498967675175d33d07be1e06993a21211b35a6eb89126fda91bdba817f4afe72aaaef269c7e6919333665b5ba629a993f11292c835078da7c632369ee386fd4c4158f4594b40b7bfe5f54a8ab20546bf5c1765e00f94beec7f444dbd78568f9151ca2cbcd911fcc374eec0edc274cb31d7744975f7bc816949189d0f6d66d79a2696718a30cddb0b991277bb0350cdddeaa5b9d969a748014f069aea5d666bfb7f005d6945e42a1246cb81e8ff24b15198b8909be710cefe44213dc09c095498e7c8414fe42f71383fe4ff0b906534f627ef9a9f0d70602638b63c43975da3f71d6c1a6ce6f7929e01e82b678a0a6c178ffaed7e7feabfada395f45ba5c3c733ea2f26fef22c0002e0bec1fa514987b10154376426968ca286d3a274f7433f013dece8105e53f0e057be0af07052a72d7e3f5cf40ad0d887cd468cd0d697975f5583dc6cec5092add13863eabf40808e982fb8fe51797d94723a346ce679a6e97c7b913c6522176212c8bd687e69590331dccf131006200670ebf49f7d5f5012173410c1bcfaf7dc6d4fe6485e41fef2bda67829b767897f681fb90f6b3c1dd6754d925a4cb74f3c94876ab59609d4f79c78a69cecefedf281baf001d5f54a3dc2cecc4bd0910a26eafecab613f0781a40aa7f1959764ad340";
	
	//토큰을 만드는 메서드
	public String create(UserEntity entity) {
		//토큰 만료시간을 설정
		//현재 시각 + 1일
		//Instant클래스 : 타임스탬프로 찍는다.
		//plus() : 첫번째 인자는 더할 양 / 두번째 인자는 시간단위
		//ChronoUnit열겨형의 DAYS 일 단위를 의미한다.
		Date expiryDate = Date.from(Instant.now().plus(1,ChronoUnit.DAYS));
		
		/*
		 * header
		 * {
		 *  "alg":"HS512"
		 * }.
		 * {
		 *  "sub":"4028838d96b357770196b369ae6e0002",
		 *  "iss":"todo app",
		 *  "iat":1595733657,
		 *  "exp":1596597657,
		 * }.
		 * 서명
		 * */
		
		//JWT 토큰을 생성
		return Jwts.builder()
				//header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512,SECRET_KEY) //헤더 + 서명 알고리즘 설정
				.setSubject(entity.getId()) //sub 클레임 : 사용자 고유 ID
				.setIssuer("todo app") //iss 클레임 : 토큰 발급자
				.setIssuedAt(new Date()) //iat 클레임 : 발급 시각
				.setExpiration(expiryDate) //exp 클레임 : 만료 시각
				.compact(); // 최종 직렬화 된 토큰 문자열 반환
	}
	
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(SECRET_KEY) //서명 검증용 키 설정
							.parseClaimsJws(token) //토큰 파싱 및 서명 검증
							.getBody(); //내부 payload(Claims) 획득
		
		return claims.getSubject(); //sub 클레임(사용자 ID) 반환
	}
	
}
