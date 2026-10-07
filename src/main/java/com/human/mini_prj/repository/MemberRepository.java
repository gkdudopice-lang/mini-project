package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    // 기본적인 CRUD는 상속을 통해서 만들어졌고, 이를 구현하는 구현체는 별도의 hibernate가 SQL 문으로 변경해줌
    Optional<Member> findByEmail(String email); // SQl : select * from member where email = ?
    boolean existsByEmail(String email); // SQL : select count(*) from member where email = ?
    Optional<Member> findByEmailAndPwd(String email, String pwd); // Sql: select * from member where email = ? amd pwd = ?


}
