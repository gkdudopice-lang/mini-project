package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 회원 정보 수정(Modify) 등 클라이언트로부터 요청 데이터를 받아올 때 사용하는 DTO 클래스입니다.
@Getter // 클라이언트가 보낸 데이터의 값을 자바 코드에서 읽어올 수 있도록 Getter 메서드들을 자동으로 생성해 줍니다.
@Setter // JSON 데이터의 필드 값들을 자바 객체의 필드에 쏙쏙 집어넣을 수 있도록 Setter 메서드들을 자동으로 생성해 줍니다.
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 만들어 주어, 스프링이 객체를 생성할 때 에러가 나지 않도록 돕습니다.
public class MemberReqDto {

    // 수정을 요청할 회원을 찾기 위한 기준이 되는 이메일 주소입니다.
    private String email;

    // 새로 변경할 회원의 이름입니다.
    private String name;

    // 새로 변경할 회원의 프로필 이미지 정보입니다.
    private String image;

    // 새로 변경할 회원의 비밀번호입니다. (아까 서비스 수정 로직에서 추가한 `setPwd()`와 짝을 이룹니다!)
    private String pwd;
}