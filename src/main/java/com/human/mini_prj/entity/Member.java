package com.human.mini_prj.entity;
// 엔티티(Entity): 데이터베이스의 테이블에 대응하는 클래스이며, @Entity가 붙은 클래스는 JPA에서 관리하며 엔티티

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Entity //이 클래스가 JPA에서 관리하는 엔티티이며, 데이터베이스의 테이블과 직접 매핑되는 클래스임을 선언합니다.
@Table(name = "member") // 매핑될 데이터베이스의 테이블 이름을 member로 지정합니다. (클래스 이름이 Member라 생략해도 기본적으로 member 테이블과 매핑되지만, 명시적으로 적어주면 가독성이 아주 좋습니다!)
@Getter // 게터 메서드 자동생성, 롬복(Lombok) 기술을 이용해 필드들의 Getter/Setter 메서드를 일일이 만들 필요 없이 자동으로 싹 생성해 줍니다.
@Setter // 세터 메서드 자동생성
@NoArgsConstructor // 매개변수가 없는 생성자 자동생성
@ToString(exclude = "pwd") // 오버라이팅으로 정보 출력시 비밀번호 제외하고 출력함

public class Member {
    @Id // 이 필드가 테이블의 기본키(Primary Key, PK)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 본키 생성 전략을 MySQL/MariaDB 등의 AUTO_INCREMENT(자동 증가) 방식을 따르도록 설정합니다. 회원이 가입할 때마다 ID가 1씩 알아서 쑥쑥 늘어납니다.
    @Column(name = "member_id") //데이터베이스 테이블의 컬럼 이름을 member_id로 매핑합니다.
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

    @PrePersist // 엔티티가 데이터베이스에 INSERT 되기 직전(저장되는 순간)에 자동으로 실행되는 메서드입니다.
    public void prePersist(){
        this.regDate = LocalDateTime.now(); // 데이터가 저장되는 바로 그 순간의 현재 시간(LocalDateTime.now())을 regDate에 쏙 집어넣어 줍니다.
    }


}