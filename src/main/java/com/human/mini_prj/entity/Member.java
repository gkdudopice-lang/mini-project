package com.human.mini_prj.entity;
// 엔티티(Entity): 데이터베이스의 테이블에 대응하는 클래스이며, @Entity가 붙은 클래스는 JPA에서 관리하며 엔티티

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity
@Table(name = "member") // 이 코드가 없어도 동일하게 동작 함.
@Getter // 게터 메서드 자동생성
@Setter // 세터 메서드 자동생성
@NoArgsConstructor // 매개변수가 없는 생성자 자동생성
@ToString(exclude = "pwd") // 오버라이팅으로 정보 출력시 비밀번호 제외하고 출력함

public class Member {
    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 생성전략을 DB 전략에 따름
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100) // 이름 필드 길이 제한
    private String name;

    @Column(nullable = false) // 비밀번호는 필수 필드
    private String pwd; // 비밀번호 저장 시 해지 암호화 적용 필요

    @Column(unique = true, length = 150) // 이메일 유니크 제약 조건 및 길이 제한
    private String email;

    @Column(length = 255) // 이미지 URL/경로 길이 제한
    private String image;

    private LocalDateTime regDate; // java.util.Kate 대신 Java 8 날짜/시간 API사용

    @PrePersist // DB에 INSERT되기 전에 실행되는 메소드
    public void prePersist(){
        this.regDate = LocalDateTime.now(); // 현재 날짜 및 시간으로 초기화
    }


}