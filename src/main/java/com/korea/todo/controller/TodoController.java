package com.korea.todo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.model.ResponseDTO;
import com.korea.todo.model.TodoDTO;
import com.korea.todo.model.TodoEntity;
import com.korea.todo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {

	//실행할 때 service객체가 필드로 직접 주입이 된다.
	@Autowired
	TodoService service;
	
	//주입받은 객체로 메서드를 실행하면 된다.
//	@GetMapping("/test")
//	public ResponseEntity<?> testTodo(){
//		//service클래스에 있는 메서드를 호출
//		String str = service.testService(); // str = TodoEntity.title("My first todo item")
//		List<String> list = new ArrayList<>();
//		list.add(str);
//		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
//		return ResponseEntity.ok().body(response);
//	}
	
	//@AuthenticationPrincipal
	//스프링 시큐리티에서 사용자의 Principal객체를 컨트롤러의 메서드의 인자로 주입하기 위해 사용하는 어노테이션
	//Principal : 시큐리티에서 현재 인증된 사용자를 나타내는 객체
	//주로 SecurityContextHolder에 저장된 Authentication객체를 통해 접근할 수 있다.
	//Authentication 객체의 getPrincipal()메서드는 인증된 사용자의 정보를 담고있으며,
	//이 정보는 @AuthenticationPrincipal 어노테이션을 통해 컨트롤러 메서드에 자동으로 주입된다.
	
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			//String tempraryUserId = "temporary-user"; //임시 유저id
			
			//TodoDTO객체를 TodoEntity객체로 변환한다.
			TodoEntity entity = TodoDTO.toEntity(dto);
			System.out.println(entity);
			
			//id 값을 명시적으로 null로 설정하여, 엔티티가 새로운 데이터임을 보장합니다.
			entity.setId(null);
			
			//임시 유저id를 설정해준다. 이 부분은 4장 인증과 인가에서 수정할 예정이다.
			//지금은 인증과 인가 기능이 없으므로 한 유저(temporary-user)만 로그인 없이 사용 가능한
			//애플리케이션인 셈이다.
			
			//진짜 유저
			entity.setUserId(userId);
			
			//서비스 레이어의 create 메서드를 호출하여, TodoEntity를 데이터베이스에 저장하는 작업을 수행한다.
			//이 메서드는 저장된 TodoEntity 객체들을 저장한 리스트를 반환한다.
			List<TodoEntity> entities = service.create(entity);
			
			//자바 스트림을 이용해 반환된 엔티디 리스트를 TodoDTO리스트로 변환한다.
            //entities.stream().map(TodoDTO::new): TodoEntity 객체의 리스트를 TodoDTO 리스트로 변환하는 과정이다.
            //.collect(Collectors.toList()): 스트림으로 변환된 객체들을 리스트로 다시 수집한다.
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
            //builder() 패턴을 사용하여 ResponseDTO 객체를 생성한다.
			//ResponseDTO : error문자열과, List<T> data필드 두가지를 가지고있다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//ResponseDTO를 반환한다.
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// 혹시 예외가 나는 경우 dto 대신 error에 메시지를 넣어 반환한다.
			String error = e.getMessage();
			
			//에러 메시지를 포함한 ResponseDTO 객체를 생성한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			//ResponseEntity.badRequest(): 400 Bad Request 상태 코드를 가진 응답을 반환한다.
			//이는 클라이언트가 잘못된 요청을 했음을 나타낸다.
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	//retriveTodoList메서드
	public ResponseEntity<?> retriveTodoList(@AuthenticationPrincipal String userId){
		//String temporaryUserId = "temporary-user";
		
		List<TodoEntity> entities = service.retrive(userId);
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		//String temporaryUserId = "temporary-user";
		
		//dto를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		entity.setUserId(userId);
		
		//서비스레이어의 update메서드를 이용해 entity를 업데이트한다.
		List<TodoEntity> entities = service.update(entity);
		
		//자바스트림을 이용해 반환된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
		
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		//String temporaryUserId = "temporary-user";
		
		//TodoDTO객체를 TodoEntity객체로 변환한다.
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		//임시유저 아이디 설정
		entity.setUserId(userId);
		
		List<TodoEntity> entities = service.delete(entity);
		
		//스트림을 이용해서 List안에 있는 Entity객체를 DTO로 바꾼다.
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//dtos 리스트를 ResponseDTO의 리스트에 담아서 반환한다.
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	
	
}

//- @Controller
//- @RestController -> @Controller + @ResponseBody
//- @Service
//- @Repository
//- @Configuration    ⇒ 전부 @Component의 자식 컴포넌트이다.