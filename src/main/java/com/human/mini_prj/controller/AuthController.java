package com.human.mini_prj.controller;

import com.human.mini_prj.dto.SignUpReqDto;
import com.human.mini_prj.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j // 로그(Log) 출력을 활성화하는 롬복 어노테이션입니다.
@RestController // 이 클래스가 REST API를 처리하며, 반환값을 자동으로 JSON으로 변환해 준다는 것을 나타냅니다.
@RequestMapping("/auth") // 이 컨트롤러 안의 모든 주소(URL)는 기본적으로 "/auth"로 시작하도록 설정합니다. (예: http://localhost:8111/auth/...)
@RequiredArgsConstructor // final이 붙은 필드(`authService`)에 자동으로 생성자를 만들어 의존성 주입(DI)을 처리합니다.
public class AuthController {

    // 인증 및 회원가입 비즈니스 로직을 처리하는 AuthService를 의존성 주입받아 선언합니다.
    private final AuthService authService;

    // 1. 회원가입 여부(이메일 중복) 확인 API (GET 방식: http://localhost:8111/auth/exists/{email})
    @GetMapping("/exists/{email}")
    public boolean existsEmail(@PathVariable String email){
        // URL 경로에 있는 이메일 값을 받아와서 이미 가입된 이메일인지 서비스에 확인 요청을 보냅니다.
        // 중복 여부에 따라 true 또는 false를 반환합니다.
        return authService.isDuplicatedEmail(email);
    }

    // 2. 회원가입 API (POST 방식: http://localhost:8111/auth/signup)
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody SignUpReqDto dto){
        // 클라이언트가 Body에 실어 보낸 회원가입 정보(JSON)를 SignUpReqDto 객체로 받아와 서비스에 전달합니다.
        // 가입 성공 여부(true/false)를 HTTP 상태 코드 200(OK)과 함께 반환합니다.
        return ResponseEntity.ok(authService.signUp(dto));
    }

    // 3. 로그인 API (POST 방식: http://localhost:8111/auth/login)
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody SignUpReqDto dto) {
        // 클라이언트가 보낸 로그인 정보(이메일, 비밀번호)를 DTO로 받아와서,
        // 서비스의 login 메서드에 이메일과 비밀번호를 각각 꺼내어 전달합니다.
        // 로그인 성공 여부(true/false)를 HTTP 상태 코드 200(OK)과 함께 반환합니다.
        return ResponseEntity.ok(authService.login(dto.getEmail(), dto.getPwd()));
    }
}