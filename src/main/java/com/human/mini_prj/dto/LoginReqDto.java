package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 로그인 요청 시 클라이언트(포스트맨 또는 프론트엔드)로부터 전달받는 데이터를 담는 DTO 클래스입니다.
@Getter // 서버에서 로그인 검증 로직을 수행할 때 입력된 이메일과 비밀번호 값을 꺼내 읽을 수 있도록 Getter를 생성합니다.
@Setter // 클라이언트가 보낸 JSON 데이터를 자바 객체의 필드에 대입(세팅)할 수 있도록 Setter를 생성합니다.
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 만들어 스프링이 객체를 원활하게 생성하도록 돕습니다.
public class LoginReqDto {

    // 로그인을 시도하는 사용자의 이메일 주소(아이디 역할)를 담는 변수입니다.
    private String email;

    // 로그인을 시도하는 사용자의 비밀번호를 담는 변수입니다.
    private String pwd;
}