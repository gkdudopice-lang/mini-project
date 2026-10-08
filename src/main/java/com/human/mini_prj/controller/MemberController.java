package com.human.mini_prj.controller;

import com.human.mini_prj.dto.MemberReqDto;
import com.human.mini_prj.dto.MemberResDto;
import com.human.mini_prj.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // 로그 출력을 위한 롬복 어노테이션입니다. (컨트롤러에서 요청/응답이나 에러를 로그로 남길 때 사용)
@RequestMapping("/member") // 이 컨트롤러 안의 모든 주소(URL)는 기본적으로 "/member"로 시작하도록 공통 경로를 지정합니다.
@RestController // 이 클래스가 REST API를 처리하는 컨트롤러이며, 반환되는 데이터를 자동으로 JSON으로 변환(직렬화)해 준다는 것을 의미합니다.
@RequiredArgsConstructor // final이 붙은 필드(`memberService`)를 매개변수로 갖는 생성자를 자동으로 만들어 의존성 주입(DI)을 처리합니다.
public class MemberController {

    // 비즈니스 로직을 처리할 MemberService를 의존성 주입(DI) 받아 선언합니다.
    private final MemberService memberService;

    // 1. 회원 전체 조회 API (GET 방식: http://localhost:8111/member/list)
    @GetMapping("/list")
    public ResponseEntity<List<MemberResDto>> findAll() {
        // 서비스의 findAll()을 호출해 전체 회원 리스트를 가져온 뒤, HTTP 상태 코드 200(OK)과 함께 클라이언트에 반환합니다.
        return ResponseEntity.ok(memberService.findAll());
    }

    // 2. 회원 상세 조회 API (GET 방식: http://localhost:8111/member/{email})
    @GetMapping("/{email}")
    public ResponseEntity<MemberResDto> findByEmail(@PathVariable String email) {
        // URL 경로에 포함된 {email} 값을 @PathVariable로 쏙 빼와서 서비스에 전달하고, 해당 회원의 정보를 응답합니다.
        return ResponseEntity.ok(memberService.findByEmail(email));
    }

    // 3. 회원 정보 수정 API (PUT 방식: http://localhost:8111/member/modify)
    @PutMapping("/modify")
    public ResponseEntity<Boolean> modifyMember(@RequestBody MemberReqDto memberReqDto) {
        // 클라이언트가 보낸 JSON 데이터(Body)를 @RequestBody를 통해 MemberReqDto 객체로 받아와 서비스에 전달합니다.
        // 수정 성공 여부(true/false)를 HTTP 200(OK) 상태 코드와 함께 반환합니다.
        return ResponseEntity.ok(memberService.modifyMember(memberReqDto));
    }

    // 4. 회원 삭제 API (DELETE 방식: http://localhost:8111/member/{email})
    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteMember(@PathVariable String email) {
        // URL 경로에 있는 이메일 값을 @PathVariable로 받아와서 해당 회원을 삭제하는 서비스 메서드를 실행합니다.
        // 삭제 성공 여부(true/false)를 HTTP 상태 코드와 함께 반환합니다.
        return ResponseEntity.ok(memberService.deleteMember(email));
    }
}