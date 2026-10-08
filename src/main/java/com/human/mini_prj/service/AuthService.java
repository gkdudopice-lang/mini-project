package com.human.mini_prj.service;


import com.human.mini_prj.dto.SignUpReqDto;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j // log 메시지 출력을 위해 사용
@Service // 이 클래스가 비즈니스 로직을 처리하는 서비스 계층임을 스프링 부트에 알려주어, 스프링 컨테이너가 관리하는 빈(Bean)으로 자동 등록되게 합니다.
@Transactional // 데이터베이스 작업을 처리할 때 사용합니다. 여러 개의 데이터 조작 작업이 한 번에 성공해야 할 때(트랜잭션), 중간에 에러가 나면 전부 원래 상태로 되돌려주는(Rollback) 안전장치 역할을 합니다.
@RequiredArgsConstructor // 생성자를 통한 의존성 주입을 자동으로 만들어 줌
public class AuthService {
    private final MemberRepository memberRepository; // 생성자를 통한 의존성 주입

    // 회원 가입 여부 확인
    public boolean isDuplicatedEmail(String email){
        return memberRepository.existsByEmail(email);
    } // 중복 체크: 전달받은 이메일이 이미 DB에 존재하는지 레포지토리에 물어본 뒤, 그 결과(true/false)를 그대로 반환합니다.

    // 회원 가입
    public boolean signUp(SignUpReqDto dto){
        if (isDuplicatedEmail(dto.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다."); // 👈 이 부분이 에러를 띄우며 가입을 막아줍니다!
        }
        try{
            Member member = toEntity(dto);
            memberRepository.save(member);
            return true;
        } catch (Exception e){
            log.error("회원 가입 시 오류 발생: {}", e.getMessage());
            return false;
        }
    } /* 회원가입 로직:

    클라이언트가 보낸 DTO(SignUpReqDto) 데이터를 받아서 엔티티(Member)로 변환합니다 (toEntity).

    memberRepository.save(member)를 호출해 DB에 저장합니다.

    성공하면 true, 중간에 예외(에러)가 터지면 catch문이 잡아서 로그를 남기고 false를 반환해 안전하게 처리합니다.
*/

    // 로그인
    public boolean login(String email,String pwd){
        Optional<Member> member = memberRepository.findByEmailAndPwd(email, pwd);
        return member.isPresent();
    }
    /* 로그인 로직:

    이메일과 비밀번호가 모두 일치하는 회원을 DB에서 찾아옵니다 (findByEmailAndPwd).

    조회 결과가 있는지(isPresent()) 확인하여, 회원이 존재하면 true(로그인 성공), 없으면 false(로그인 실패)를 반환합니다.
*/
    // DTO -> Entity
    private Member toEntity(SignUpReqDto dto){
        Member member = new Member();
        member.setName(dto.getName());
        member.setPwd(dto.getPwd());
        member.setEmail(dto.getEmail());
        return member;
    }
}
/*
toEntity(SignUpReqDto dto):

클라이언트(프론트엔드나 포스트맨)가 보낸 데이터는 통신용 객체인 DTO(SignUpReqDto)에 담겨서 들어옵니다.

하지만 데이터베이스 테이블과 직접 매핑되는 것은 엔티티(Member) 클래스이기 때문에, DTO에 담긴 이름(name), 비밀번호(pwd), 이메일(email) 값을 꺼내서 새로운 Member 객체에 꾹꾹 눌러 담아주는 역할을 합니다.

이 과정을 거쳐야 memberRepository.save(member)를 통해 안전하게 데이터베이스에 저장할 수 있습니다. (private으로 선언되어 있어서 이 서비스 클래스 내부에서만 비밀스럽게 사용되는 헬퍼 메서드입니다!)
 */