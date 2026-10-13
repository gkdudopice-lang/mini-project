package com.human.mini_prj.service;


import com.human.mini_prj.dto.BoardResDto;
import com.human.mini_prj.dto.BoardWriteReqDto;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.BoardRepository;
import com.human.mini_prj.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 게시글 전체 조회
    public List<BoardResDto> getBoardList(){
        List<Board> boardList = boardRepository.findAllByOrderByRegTimeDesc();
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : boardList){
            boardResDtoList.add(toDto(board));
        }
        return boardResDtoList;
    }

    // 게시글 상세 조회
    public BoardResDto getBoardDetail(Long id) throws IllegalAccessException {
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()){
            throw new IllegalAccessException("해당 게시글이 존재하지 않습니다.");
        }
        return toDto(board.get());
    }

    // 게시글 작성
    public boolean writeBoard(BoardWriteReqDto dto){
        try{
            // 작성자 조회
            Optional<Member> member = memberRepository.findByEmail(dto.getEmail());
            if (member.isEmpty()){
                throw new IllegalAccessException("해당 회원이 존재하지 않습니다: " + dto.getEmail());

            }
            Board board = new Board();
            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            board.setMember(member.get());
            boardRepository.save(board);
            return true;

        } catch(Exception e){
            log.error("게시글 작성 시 오류 발생 : {}", e.getMessage());
            return false;
        }
    }

    // 게시글 수정
    public boolean updateBoard(Long id, BoardWriteReqDto dto) {
        try {
            Optional<Board> optionalBoard = boardRepository.findById(id);
            if (optionalBoard.isEmpty()) {
                throw new IllegalAccessException("해당 게시글이 존재하지 않습니다.");
            }
            Board board = optionalBoard.get();
            Member member = board.getMember();
            String email = member.getEmail();

            // 작성자 이메일 검증
            if (!email.equals(dto.getEmail())) {
                throw new IllegalAccessException("작성자만 수정할 수 있습니다."); // 세미콜론 추가
            }

            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            log.error("게시글 수정 오류: {}", e.getMessage());
            return false;
        }
    }

    // 게시글 삭제
    public boolean deleteBoard(Long id) {
        try {
            Optional<Board> optionalBoard = boardRepository.findById(id);
            if (optionalBoard.isEmpty()) {
                throw new IllegalAccessException("해당 게시글이 존재하지 않습니다.");
            }
            boardRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("게시글 삭제 오류: {}", e.getMessage());
            return false;
        }
    }


    // Entity -> Dto
    private BoardResDto toDto(Board board){
        BoardResDto dto = new BoardResDto();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setEmail(board.getMember().getEmail());
        dto.setName(board.getMember().getName());
        dto.setRegDate(board.getRegTime());
        return dto;
    }
}
