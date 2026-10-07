package com.human.mini_prj.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j // log 메시지 출력을 위해 사용
@Service // spring Container에 Bean 등록
@Transactional // 트랜젝션 처리: 여러개의 물리적인 작업 단어를 한개의 논리적인 단위로 묶음
@RequiredArgsConstructor // 생성자를 통한 의존성 주입을 자동으로 만들어 줌
public class AuthService {
}
