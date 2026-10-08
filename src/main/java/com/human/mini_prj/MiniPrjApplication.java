package com.human.mini_prj; // 패키지 선언 이 클래스가 속해 있는 폴더 경로를 나타냄

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 1. 필요한 Bean 자동 설정 2. 캄포넌트 스캔: @붙은 클래스들을 스프링 컨테이너에 자동 등록 3. 스프링 부트 설정: 이 클래스가 프로젝트의 설정 클래스임을 명시
public class MiniPrjApplication { // 메인 어플리케이션 클래스: 자바 프로그램이 실행되기 위한 진입점 역할을 함

	public static void main(String[] args) {
		SpringApplication.run(MiniPrjApplication.class, args);
	}// 내장 톰캣 서버가 켜지고, 데이터 베이스와 연동되며 미니 프로젝트 서버가 구동됩니다.

}
