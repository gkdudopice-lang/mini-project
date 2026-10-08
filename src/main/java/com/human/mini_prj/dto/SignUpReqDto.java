package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 회원가입 요청 시 클라이언트로부터 전달받는 데이터를 담는 DTO(Data Transfer Object) 클래스입니다.
@Setter // 필드들의 값을 설정하기 위한 Setter 메서드(setXxx)들을 롬복이 자동으로 생성해 줍니다.
@Getter // 필드들의 값을 가져오기 위한 Getter 메서드(getXxx)들을 롬복이 자동으로 생성해 줍니다.
@NoArgsConstructor // 파라미터가 없는 기본 생성자(Default Constructor)를 롬복이 자동으로 생성해 줍니다. (역직렬화 등에 필수)
public class SignUpReqDto {

    // 사용자가 입력한 이메일 주소를 담는 변수입니다. (회원 아이디 역할)
    private String email;

    // 사용자가 입력한 비밀번호를 담는 변수입니다.
    private String pwd;

    // 사용자가 입력한 이름을 담는 변수입니다.
    private String name;
}