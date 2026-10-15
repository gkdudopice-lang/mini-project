package com.human.mini_prj.config;

import com.human.mini_prj.entity.Board;
import com.human.mini_prj.entity.Comment;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.BoardRepository;
import com.human.mini_prj.repository.CommentRepository;
import com.human.mini_prj.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInit {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository; // 댓글 리포지토리 추가

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // 이미 데이터가 존재하면 중복 생성하지 않고 종료
            if (memberRepository.count() > 0) {
                return;
            }

            // 21명의 테스트 회원, 게시글, 댓글 생성
            for (int i = 1; i <= 21; i++) {
                // 1. 회원 생성
                Member member = new Member();
                member.setEmail("user" + i + "@test.com");
                member.setPwd("password123");
                member.setName("테스트사용자" + i);
                member.setRegDate(LocalDateTime.now().minusDays(25 - i)); // 날짜를 조금씩 다르게 부여 (최신순 정렬 테스트용)
                memberRepository.save(member);

                // 2. 게시글 생성 (회원당 1개씩 연결)
                Board board = new Board();
                board.setTitle("테스트 제목 " + i);
                board.setContent("테스트 내용 " + i);
                board.setMember(member);
                board.setRegTime(LocalDateTime.now().minusDays(25 - i));
                boardRepository.save(board);

                // 3. 댓글 생성 (각 게시글당 댓글도 1~2개씩 연동)
                Comment comment = new Comment();
                comment.setContent("테스트 댓글입니다 " + i);
                comment.setBoard(board);
                comment.setMember(member);
                comment.setRegDate(LocalDateTime.now().minusDays(25 - i));
                commentRepository.save(comment);
            }

            System.out.println(">>> [DataInit] 테스트용 회원, 게시글, 댓글 더미 데이터 자동 생성 완료! <<<");
        };
    }
}