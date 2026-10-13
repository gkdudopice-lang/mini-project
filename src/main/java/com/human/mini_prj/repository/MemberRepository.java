package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository // 인터페이스와 데이터베이스가 소통하는 저장소 역할을 한다는걸 스프링 부트에 알려주는 것
public interface MemberRepository extends JpaRepository<Member, Long>{ // 스프링 데이터 JPA가 제공하는 JpaRepository를 상속받습니다.
    //<Member, Long>은 각각 "어떤 엔티티(테이블)를 다루는지(Member)"와 "그 엔티티의 기본키(Primary Key) 타입(Long, 회원 번호 타입)"을 의미합니다.
    // 기본적인 CRUD는 상속을 통해서 만들어졌고, 이를 구현하는 구현체는 별도의 hibernate가 SQL 문으로 변경해줌 = 이것만 상속받으면 저장(save), 전체 조회(findAll), 수정, 삭제(delete) 같은 기본적인 SQL 문들을 우리가 직접 코딩하지 않아도 스프링이 알아서 다 만들어 줍니다!
    Optional<Member> findByEmail(String email); // SQl : select * from member where email = ?, 이메일로 회원 찾기: 결과가 없으수도 있으니 안전하게 Optional로 감싸서 반환합니다.
    boolean existsByEmail(String email); // SQL : select count(*) from member where email = ? // 이메일 중복 확인 (존재 여부): 해당 이메일이 데이터베이스에 이미 가입되어 있는지 확인합니다. 있으면 true, 없으면 false를 척척 반환해 줍니다.
    Optional<Member> findByEmailAndPwd(String email, String pwd); // Sql: select * from member where email = ? amd pwd = ?, 로그인 검증: 이메일(email)과 비밀번호(pwd)가 동시에 일치하는 회원을 찾습니다. 로그인 로직에서 사용자가 입력한 정보가 정확한지 대조할 때 아주 유용하게 쓰입니다.

    // 1. 이름이 정확히 일치하는 회원 목록 조외
    List<Member> findByName(String name);

    // 2. 이름에 특정 글자가 포함된 회원 조회
    List<Member> findByNameContaining(String keyword);

    // 3. 특정 도메인(예: @gmail.com)으로 끝나는 이메일을 가진 회원 조회
    List<Member> findByEmailEndingWith(String domain);

    // 4. 이름 또는 이메일이 일치하는 회운 조회
    List<Member> findByNameOrEmail(String name, String email);

    // 5. 특정 일시 이후에 가입한 회원 조회
    List<Member> findByRegDateAfter(LocalDateTime regDate);

    // 6. 두 일시 사이에 가입한 회원 조회
    List<Member> findByRegDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 7. 전체 회원을 가입일 최신순으로 조회
    List<Member> findAllByOrderByRegDateDesc();

    // 8. 가장 최근에 가입한 회원 3명만 조회
    List<Member> findTop3ByOrderByRegDateDesc();

    // 9. 대소문자 구분 없이 이름에 키워드가 포함된 회원을 이름 오름차순으로 조회
    List<Member> findByNameContainingIgnoreCaseOrderByNameAsc(String keyword);

    // 10. 이메일로 회원삭제
    long deleteByEmail(String email);

}
