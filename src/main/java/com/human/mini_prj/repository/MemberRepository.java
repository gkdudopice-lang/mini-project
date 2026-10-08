package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 인터페이스와 데이터베이스가 소통하는 저장소 역할을 한다는걸 스프링 부트에 알려주는 것
public interface MemberRepository extends JpaRepository<Member, Long>{ // 스프링 데이터 JPA가 제공하는 JpaRepository를 상속받습니다.
    //<Member, Long>은 각각 "어떤 엔티티(테이블)를 다루는지(Member)"와 "그 엔티티의 기본키(Primary Key) 타입(Long, 회원 번호 타입)"을 의미합니다.
    // 기본적인 CRUD는 상속을 통해서 만들어졌고, 이를 구현하는 구현체는 별도의 hibernate가 SQL 문으로 변경해줌 = 이것만 상속받으면 저장(save), 전체 조회(findAll), 수정, 삭제(delete) 같은 기본적인 SQL 문들을 우리가 직접 코딩하지 않아도 스프링이 알아서 다 만들어 줍니다!
    Optional<Member> findByEmail(String email); // SQl : select * from member where email = ?, 이메일로 회원 찾기: 결과가 없으수도 있으니 안전하게 Optional로 감싸서 반환합니다.
    boolean existsByEmail(String email); // SQL : select count(*) from member where email = ? // 이메일 중복 확인 (존재 여부): 해당 이메일이 데이터베이스에 이미 가입되어 있는지 확인합니다. 있으면 true, 없으면 false를 척척 반환해 줍니다.
    Optional<Member> findByEmailAndPwd(String email, String pwd); // Sql: select * from member where email = ? amd pwd = ?, 로그인 검증: 이메일(email)과 비밀번호(pwd)가 동시에 일치하는 회원을 찾습니다. 로그인 로직에서 사용자가 입력한 정보가 정확한지 대조할 때 아주 유용하게 쓰입니다.


}
