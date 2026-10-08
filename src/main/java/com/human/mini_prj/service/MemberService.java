package com.human.mini_prj.service;

import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.human.mini_prj.dto.MemberReqDto;
import com.human.mini_prj.dto.MemberResDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j // 로깅(로그 출력) 기능을 사용하기 위한 롬복 어노테이션입니다. (log.info, log.error 등 사용 가능)
@Service // 이 클래스가 스프링 부트의 비즈니스 로직을 담당하는 서비스 계층(Bean)임을 스프링에 등록합니다.
@Transactional // 클래스 내의 모든 메서드가 실행될 때 트랜잭션을 보장하며, 오류 발생 시 자동 롤백 처리를 돕습니다.
@RequiredArgsConstructor // final이 붙거나 @NonNull인 필드를 모아 자동으로 생성자를 만들어 의존성 주입(DI)을 처리합니다.
public class MemberService {

    // 데이터베이스와 소통하는 Repository 인터페이스를 선언합니다. (불변성을 위해 final 사용)
    private final MemberRepository memberRepository;

    // 1. 회원 전체 조회 메서드
    public List<MemberResDto> findAll() {
        // DB에 저장된 모든 회원 데이터를 Entity 리스트 형태로 가져옵니다.
        List<Member> members = memberRepository.findAll();

        // 컨트롤러나 클라이언트로 전달할 DTO 리스트를 새로 생성합니다.
        List<MemberResDto> memberResDtos = new ArrayList<>();

        // 조회한 Entity 리스트를 반복문으로 순회합니다.
        for (Member member : members) {
            // 각각의 Entity를 DTO로 변환한 뒤, 결과 DTO 리스트에 추가합니다.
            memberResDtos.add(convertEntityToDto(member));
        }

        // 최종적으로 변환된 DTO 리스트를 반환합니다.
        return memberResDtos;
    }

    // 2. 회원 상세 조회 메서드 (이메일을 기준으로 단건 조회)
    public MemberResDto findByEmail(String email) {
        // 전달받은 이메일로 DB를 조회하고, 데이터가 없으면 RuntimeException(예외)을 발생시킵니다.
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("해당 이메일의 사용자를 찾을 수 없습니다.")  // 에러 발생 시 전달할 메시지
        );

        // 조회된 Entity를 DTO로 변환해서 반환합니다.
        return convertEntityToDto(member);
    }

    // 3. 회원 정보 수정 메서드
    public boolean modifyMember(MemberReqDto memberReqDto) {
        try {
            // 수정할 대상 회원이 DB에 존재하는지 이메일로 먼저 찾습니다. 없으면 예외 발생!
            Member member = memberRepository.findByEmail(memberReqDto.getEmail()).orElseThrow(
                    () -> new RuntimeException("해당 이메일의 사용자를 찾을 수 없습니다.")
            );

            // DTO에서 전달받은 새로운 이름 값으로 엔티티의 이름을 변경합니다.
            member.setName(memberReqDto.getName());

            // DTO에서 전달받은 새로운 이미지 값으로 엔티티의 이미지를 변경합니다.
            member.setImage(memberReqDto.getImage());

            // DTO에서 전달받은 새로운 비밀번호 값으로 엔티티의 비밀번호를 변경합니다.
            member.setPwd(memberReqDto.getPwd());

            // 변경된 내용이 반영된 엔티티를 데이터베이스에 다시 저장(Update)합니다.
            memberRepository.save(member);

            // 수정 작업이 성공했으므로 true를 반환합니다.
            return true;

        } catch (Exception e) {
            // 수정 과정에서 에러가 발생하면 로그에 에러 내용을 기록합니다.
            log.error("회원 정보 수정 실패 : {}", e.getMessage());

            // 실패했으므로 false를 반환합니다.
            return false;
        }
    }

    // 4. 회원 삭제 메서드
    public boolean deleteMember(String email) {
        try {
            // 삭제할 회원이 DB에 존재하는지 이메일로 찾습니다. 없으면 예외 발생!
            Member member = memberRepository.findByEmail(email).orElseThrow(
                    () -> new RuntimeException("해당 회원이 존재 하지 않습니다.")
            );

            // 찾아낸 회원 엔티티를 데이터베이스에서 삭제합니다.
            memberRepository.delete(member);

            // 삭제 작업이 성공했으므로 true를 반환합니다.
            return true;

        } catch (Exception e) {
            // 삭제 과정에서 에러가 나면 에러 로그를 남깁니다.
            log.error("회원 정보 삭제 실패 : {}", e.getMessage());

            // 실패했으므로 false를 반환합니다.
            return false;
        }
    }

    // 5. Entity 객체를 DTO 객체로 변환해 주는 내부 헬퍼 메서드
    private MemberResDto convertEntityToDto (Member member) {
        // 비어있는 새로운 응답 DTO 객체를 생성합니다.
        MemberResDto memberResDto = new MemberResDto();

        // Entity의 이메일 값을 꺼내 DTO에 세팅합니다.
        memberResDto.setEmail(member.getEmail());

        // Entity의 비밀번호 값을 꺼내 DTO에 세팅합니다.
        memberResDto.setPwd(member.getPwd());

        // Entity의 이름 값을 꺼내 DTO에 세팅합니다.
        memberResDto.setName(member.getName());

        // Entity의 이미지 값을 꺼내 DTO에 세팅합니다.
        memberResDto.setImage(member.getImage());

        // Entity의 회원가입일(등록일) 값을 꺼내 DTO에 세팅합니다.
        memberResDto.setRegDate(member.getRegDate());

        // 데이터가 모두 채워진 DTO 객체를 반환해 줍니다.
        return memberResDto;
    }
}