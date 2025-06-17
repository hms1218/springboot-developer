package com.korea.todo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.korea.todo.dto.TestRequestBodyDTO;
import com.korea.todo.model.ResponseDTO;

//Controller
//클라이언트로부터 요청을 받아오거나, 서버로부터 응답을 내보내는 역할

@RestController
//데이터를 반환하는 컨트롤러로 사용
//html같은 뷰 페이지를 반환하는 대신, JSON이나 XML형식의 데이터를 반환하는
//RESTful API를 제공하는데 적합하다.
//@Controller, @ResponseBody 두 개의 어노테이션의 결합이다.
//@ResponseBody는 메서드의 반환값을 HTTP ResponseBody로 직렬화해 클라이언트에게 전달한다.

@RequestMapping("test") //test주소("주소")로 요청이 들어왔을 때 현재 컨트롤러로 들어올 수 있게 해준다.

//@PathVariable
//URL 경로의 일부를 변수로 사용할 수 있도록 지원하는 어노테이션
//URL에 포함된 값을 동적으로 받아와 메서드의 매개변수로 사용할 수 있다.
// {}템플릿 변수와 매개변수의 이름이 같을 때, 스프링이 요청 URI에서 값을 추출해 자동으로 변수를 채워준다.
//이름을 다르게 쓰고싶으면 @PathVariable() -> ()에 지정하면 된다.
//- required 속성(필수여부 true/false)

//@RequestParam
//HTTP 요청의 쿼리 파라미터나 폼 데이터를 처리하기 위해 사용되는 어노테이션이다.
//클라이언트가 서버에 요청을 보낼 때, URL 뒤에 붙이는 쿼리스트링이나 폼 데이터를 받아서
//메서드의 파라미터로 전달할 수 있게 해준다.

//쿼리스트링
//localhost:10000/users?id=xxx&pw=xxx

//POST로 요청시 URL에 쿼리스트링이 안붙는다.
//그래도 폼데이터를 처리하는데 사용할 수 있다.

//@RequestParam과 @PathVariable의 차이점
//@RequestParam
//1.주로 쿼리파라미터를 처리한다. 또한, POST 요청에서 form 데이터도 처리할 수 있다.

//@PathVariable
//1.URL 경로의 일부로 전달되는 변수를 처리한다.
//2.주로 RESTful API에서 리소스를 식별하기 위해 사용된다.

//@RequestBody
//HTTP 요청의 본문(body)에 담긴 데이터를 자바 객체로 변환하여 컨트롤러에 있는 메서드의 매개변수로 전달하는 어노테이션이다.
//주로 POST, PUT, CATCH 요청에서 사용되며, 
//클라이언트가 전송한 JSON,XML 또는 폼 데이터 등을 자바 객체로 변환하는 역할을 한다.

//</form>
//HTTP요청의 본문(body)에 담긴 데이터를 자바 객체로 변환하여 컨트롤러 메서드의 매개변수로 전달하는 데 사용하는 어노테이션

//주요기능
// 1. HTTP 요청 본문 처리
// - 요청이 들어올 때 JSON이나 XML 형식으로 보내진다.
// - 스프링이 자동으로 자바 객체로 변환한다.

//@ResponseBody
//컨트롤러 메서드의 반환값을 HTTP 응답의 본문(body)에 직접 포함시킬 때 사용된다.
//HTML 같은 템플릿을 반환하는 대신, 주로 JSON, XML, 문자열과 같은 데이터를 클라이언트에게 반환하는데 사용된다.

//ResponseEntity
//스프링에서 제공하는 클래스로 HTTP응답을 보다 세밀하게 제어할 수 있는 방법을 제공한다.
//HTTP 상태코드, 헤더, 응답 body부분을 포함한 전체 HTTP응답을 구성할 수 있으며, 주로 REST API에서 많이 사용된다.

//주요기능
//1. HTTP 상태 코드 제어
// - ResponseEntity를 사용하면 클라이언트에 응답할 때, HTTP상태 코드를 명시적으로 설정할 수 있다.
//2. HTTP 헤더 제어
// - 응답에 HTTP 헤더를 추가하거나 수정할 수 있따.
// - 이를 통해 캐시 제어, 인증 정보, 콘텐츠 타입을 제어할 수 있다.
//3. 응답 본문 제어
// - 응답 본문(body)에 객체나 JSON데이터를 포함할 수 있다.
// - 이 객체는 스프링에서 JSON 또는 XML로 직렬화되어 클라이언트에게 반환된다.

//주요 메서드
//ok() : 200 OK상태코드로 응답하는 빌더 메서드
//status(HttpStatus status) : 특정 상태 코드를 반환하는 메서드
//noContent() : 204 No Content응답을 반환하는 메서드
//badRequest() : 400 Bad Request응답을 반환하는 메서드
//notFound() : 404 Not Found응답을 반환하는 메서드
public class TestController {

	//localhost:10000/test/testGetMapping
	@GetMapping("/testGetMapping") //GET으로 요청이 들어왔을 때 요청을 받아서 아래 메서드를 실행해준다.
	public String testController() {
		return "Hello world";
	}

	//localhost:10000/test/users/1
//	@GetMapping({"/users","/users/{id}"})
//	//배열을 통해 여러개의 요청 URI를 받을 수 있다.
//	public String getUserById(@PathVariable(name="id", required=false) int userId) {
//		return "User ID : " + userId;
//	}
	
	//정규식을 써서 변수 형식을 제한할 수 있다.
//	@GetMapping("/users/{userId:[0-9]{3}}")
//	public String getOrderByUserAndOrderId(@PathVariable("userId") Long id) {
//		return "User ID : " + id;
//	}
	
	//localhost:10000/test/users?id=100
	//쿼리스트링의 key와 매개변수의 변수명이 일치한다면 value값을 안줘도 된다.
	//@RequestParam(required=false)
	//값을 필수로 넣지 않아도 에러가 나지는 않는다.
	//@RequestParam(defaultValue = "0")
	//값이 넘어오지 않았을 때 기본값을 설정할 수 있다.
	@GetMapping("/users")
	public String getUserById(@RequestParam(value="id") Long userId) {
		return "User ID : " + userId;
	}
	
	//localhost:10000/test/search?query=spring&page=2
	@GetMapping("/search")
	public String search(@RequestParam("query") String query, @RequestParam("page") int page) {
		return "Search query: " + query + ", page : " + page;
	}
	
	@PostMapping("/submitForm")
	public String submitForm(@RequestParam("name") String name, @RequestParam("email") String email) {
		return "Form sumbitted : Name = " + name + ", Email = " + email;
	}
	
	@GetMapping("/testRequestBody")
	//JSON형식으로 전달되는 데이터를 TestRequestBodyDTO형식으로 만들어서 넣어준다.
	//{"id" : 123, "message" : "Hello ?"}
	public String testRequestBody(@RequestBody TestRequestBodyDTO dto) {
		return "ID : " + dto.getId() + ", Message : " + dto.getMessage();
	}
	
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testResponseBody(){
		List<String> list = Arrays.asList("하나","둘","셋");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//String error, List<String> list가 담겨있는
		//ResponseDTO 객체를 반환
		return response;
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testResponseEntity(){
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.badRequest().body(response);
	}
	//ResponseDTO를 반환하는 것과 비교했을 때 큰 차이는 없지만
	//단지 헤더와 HTTPStatus를 조작할 수 있다는 점이 다르다.
	
}
