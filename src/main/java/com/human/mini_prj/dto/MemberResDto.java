package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime; // 날짜와 시간 정보를 다루기 위한 자바 기본 라이브러리입니다.

// 데이터베이스에 저장된 회원 정보를 조회(전체/상세)한 뒤, 클라이언트에게 응답할 때 사용하는 DTO 클래스입니다.
@Getter // 응답 데이터의 필드 값들을 읽어갈 수 있도록 Getter 메서드들을 자동으로 생성해 줍니다.
@Setter // 데이터 값을 집어넣을 수 있도록 Setter 메서드들을 자동으로 생성해 줍니다.
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 만들어 줍니다.
public class MemberResDto {

    // 회원의 이메일 주소를 담는 변수입니다. (조회 결과 출력용)
    private String email;

    // 회원의 비밀번호를 담는 변수입니다.
    // (보통 실무에서는 보안상 비밀번호를 응답에 포함하지 않지만, 현재 학습/테스트용으로는 포함되어 있습니다.)
    private String pwd;

    // 회원의 이름을 담는 변수입니다.
    private String name;

    // 회원의 프로필 이미지 경로나 URL을 담는 변수입니다.
    private String image;

    // 회원이 가입한 날짜와 시간(Timestamp)을 담는 변수입니다.
    private LocalDateTime regDate;
}