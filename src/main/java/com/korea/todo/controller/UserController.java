package com.korea.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.dto.UserDTO;
import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.UserEntity;
import com.korea.todo.security.TokenProvider;
import com.korea.todo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

	//userService 생성자 주입하기
	private final UserService service;
	
	//TokenProvider 클래스 주입받기
	private final TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	//회원가입
	//로그인을 해야 토큰을 주는거지, 회원가입을 했다고 토큰을 주는것이 아니다.
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO dto){
		//요청본문에 포함된 UserDTO 객체를 수신하여 처리한다.
		try {
			//UserDTO기반으로 UserEntity객체 생성하기
			UserEntity entity = UserEntity.builder()
										.username(dto.getUsername())
										.password(passwordEncoder.encode(dto.getPassword()))
										.build();
			//UserEntity객체를 service로 보내서 데이터베이스에 추가하기
			UserEntity responseUserEntity = service.create(entity);
			
			//등록된 UserEntity 정보를 UserDTO로 변환하여 응답에 사용한다.
			UserDTO responseUserDTO = UserDTO.builder()
											.id(responseUserEntity.getId())
											.username(responseUserEntity.getUsername())
											.build();
			
			return ResponseEntity.ok(responseUserDTO);
				
		} catch (Exception e) {
			//예외가 발생한 경우, 에러 메시지를 포함한 ResponseDTO객체를 만들어 응답에 보낸다.
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
			
			return ResponseEntity
					.badRequest() //HTTP 400응답을 생성한다.
					.body(responseDTO); //에러 메시지를 포함한 응답 본문을 반환한다.
		}
	}
	
	//로그인을 Get으로 만들면 브라우저의 주소창에 아이디와 비밀번호가 노출될 수 있다.
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO dto){
		//요청 본문으로 전달된 UserDTO의 username과 password를 기반으로 유저를 조회
		UserEntity user = service.getByCredential(
										dto.getUsername(), 
										dto.getPassword(),
										passwordEncoder);
		
		//조회된 user가 있으면
		if(user != null) {
			//토큰을 만든다.
			final String token = tokenProvider.create(user);
			
			
			//인증에 성공한 경우 유저 정보를 UserDTO로 반환하여 응답에 사용한다.
			final UserDTO responseUserDTO = UserDTO.builder()
												.id(user.getId())
												.username(user.getUsername())
												.token(token)
												.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		}else {
			//조회된 유저가 없거나, 인증이 실패한 경우 에러 메시지를 포함한 ResponseDTO를 반환한다.
			ResponseDTO responseDTO = ResponseDTO.builder()
											.error("Login failed")
											.build();
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
		
		
		
	}
	
	
}

//로그인 유지 구현
//모든 요청 API에 토큰을 보낸다
//각 API들은 토큰을 확인하고 접근 허용 또는 거부하는 코드를 실행
//문제는 모든 API가 이 작업을 해야한다는것
//createTodo, getTodo, updateTodo, deleteTodo 네 개의 API가 존재한다.
//만약 50개가 넘는 API를 관리한다면 50번이나 같은 코드를 작성해야한다는 문제가 발생한다.

//스프링 시큐리티를 이용해 코드를 한번만 작성하고, 이 코드가 모든 API를 수행하기 바로 전에 실행되도록 구현해보자.

//JWT 생성 및 반환을 구현
//유저 정보를 바탕으로 헤더와 페이로드를 작성하고 전자 서명을 한 후 토큰을 반환할 것이다.
//구현을 위해 JWT관련 라이브러리를 gradle의존성에 추가할 것이다.

//create()메서드는 JWT라이브러리를 이용해 JWT토큰을 생성한다.
//토큰을 생성하는 과정에서 우리는 임의로 지정한 SECRET_KEY를 개인키로 사용한다.
//validateAndGetUserId()는 토큰을 디코딩, 파싱 및 위조여부를 확인한다.
//이후에 우리가 원하는 유저의 ID를 반환한다.
//라이브러리 덕분에 JSON을 생성, 서명, 인코딩, 디코딩, 파싱하는 작업을 직접하지 않아도 된다.
//TokenProvider를 작성했으니, 로그인을 하면 create()메서드를 통해 토큰을 생성하고
//UserDTO에 실어서 보낸다.




