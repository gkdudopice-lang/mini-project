package com.human.mini_prj.service;


import com.human.mini_prj.dto.BoardResDto;
import com.human.mini_prj.dto.BoardWriteReqDto;
import com.human.mini_prj.dto.MemberResDto;
import com.human.mini_prj.dto.PageResDto;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.entity.BoardLike;
import com.human.mini_prj.repository.BoardRepository;
import com.human.mini_prj.repository.BoardLikeRepository;
import com.human.mini_prj.repository.CommentRepository;
import com.human.mini_prj.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;

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
        Board foundBoard = board.get();

        foundBoard.setViewCount(foundBoard.getViewCount() + 1);
        return toDto(foundBoard);
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

    // 게시글 목록 페이지네이션 조회(최신순)
    public PageResDto<BoardResDto> getBoardPage(int page, int size){
        // PageRequest.of(페이지 번호, 페이지 크기, 정렬)
        // - 페이지번호는 0부터 시작
        // - Sort.by(정렬방향, "엔티티 필드명")
        Pageable pageable = PageRequest.of(page, size, Sort.by("regTime").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        Page<BoardResDto> BoardResDtoPage = boardPage.map(this::toDto);

        return new PageResDto<>(BoardResDtoPage);
    }

    // 게시글 좋아요
    public Map<String, Object> toggleLike(Long boardId, String email) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isEmpty()) {
            throw new RuntimeException("해당 게시글이 존재하지 않습니다.");
        }
        Board board = optionalBoard.get();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new RuntimeException("해당 회원이 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        Optional<BoardLike> optionalLike = boardLikeRepository.findByBoardIdAndMemberEmail(boardId, email);

        boolean liked;

        if (optionalLike.isPresent()) {
            boardLikeRepository.delete(optionalLike.get());
            liked = false;
        } else {
            BoardLike boardLike = new BoardLike();
            boardLike.setBoard(board);
            boardLike.setMember(member);
            boardLikeRepository.save(boardLike);
            liked = true;
        }

        long likeCount = boardLikeRepository.countByBoardId(boardId);

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);

        return result;
    }

    // 게시글 검색 + 페이지네이션
    public PageResDto<BoardResDto> searchBoards(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("regTime").descending());
        Page<Board> boardPage = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        Page<BoardResDto> boardResDtoPage = boardPage.map(this::toDto);

        return new PageResDto<>(boardResDtoPage);
    }

    // 내가 쓴 글 목록 조회 + 페이지네이션
    public PageResDto<BoardResDto> getMyBoards(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("regTime").descending());
        Page<Board> boardPage = boardRepository.findByMemberEmail(email, pageable);
        Page<BoardResDto> boardResDtoPage = boardPage.map(this::toDto);

        return new PageResDto<>(boardResDtoPage);
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
        dto.setViewCount(board.getViewCount());
        dto.setCommentCount(commentRepository.countByBoardId(board.getId()));
        dto.setLikeCount(boardLikeRepository.countByBoardId(board.getId()));
        return dto;
    }


}
