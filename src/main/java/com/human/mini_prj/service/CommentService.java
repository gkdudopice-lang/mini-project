package com.human.mini_prj.service;

import com.human.mini_prj.dto.CommentReqDto;
import com.human.mini_prj.dto.CommentResDto;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.entity.Comment;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.BoardRepository;
import com.human.mini_prj.repository.CommentRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    public boolean writeComment(CommentReqDto dto){
        try{
            Optional<Board> board = boardRepository.findById(dto.getBoardId());
            if(board.isEmpty()){
                throw new IllegalAccessException("해당 게시글이 존재하지 않습니다.");
            }

            Optional<Member> member = memberRepository.findByEmail(dto.getEmail());
            if (member.isEmpty()){
                throw new IllegalAccessException("해당 회원이 존재하지 않습니다: " + dto.getEmail());
            }

            Comment comment = new Comment();
            comment.setContent(dto.getContent());
            comment.setBoard(board.get());
            comment.setMember(member.get());

            commentRepository.save(comment);
            return true;
        } catch (Exception e){
            log.error("댓글 작성 실패: {}", e.getMessage());
            return false;
        }
    }

    // 2. 특정 게시글의 댓글 목록 조회
    public List<CommentResDto> getCommentList(Long boardId){
        List<Comment> comments = commentRepository.findByBoardIdOrderByRegDateDesc(boardId);
        List<CommentResDto> commentResDtos = new ArrayList<>();

        for (Comment comment : comments) {
            commentResDtos.add(convertEntityToDto(comment));
        }
        return commentResDtos;
    }

    // 3. 댓글 수정
    public boolean updateComment(Long commentId, CommentReqDto dto){
        try{
            Optional<Comment> comment = commentRepository.findById(commentId);
            if(comment.isEmpty()){
                throw new IllegalAccessException("해당 댓글이 존재하지 않습니다.");
            }

            Comment targetComment = comment.get();
            if (!targetComment.getMember().getEmail().equals(dto.getEmail())){
                throw new IllegalAccessException("작성자만 수정할 수 있습니다.");
            }

            targetComment.setContent(dto.getContent());
            commentRepository.save(targetComment);
            return true;
        } catch (Exception e){
            log.error("댓글 수정 실패: {}", e.getMessage());
            return false;
        }
    }

    // 4. 댓글 삭제
    public boolean deleteComment(Long commentId){
        try{
            if (!commentRepository.existsById(commentId)){
                throw new IllegalAccessException("해당 댓글이 존재하지 않습니다.");
            }
            commentRepository.deleteById(commentId);
            return true;
        } catch (Exception e){
            log.error("댓글 삭제 실패: {}", e.getMessage());
            return false;
        }
    }
    // Entity -> Dto 컨트롤러나 조회 메서드에서 엔티티(Comment)를 그대로 반환하는 대신, 깔끔하게 DTO(CommentResDto)로 변환해서 반환하도록 맞춰주어야 보안상으로도 좋고 앞서 만들었던 Board나 Member 시스템과 일관성이 유지됩니다.
    private CommentResDto convertEntityToDto(Comment comment) {
        CommentResDto dto = new CommentResDto();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setRegDate(comment.getRegDate());

        // 연관된 Board와 Member에서 필요한 정보 추출
        if (comment.getBoard() != null) {
            dto.setBoardId(comment.getBoard().getId());
        }
        if (comment.getMember() != null) {
            dto.setEmail(comment.getMember().getEmail());
            dto.setName(comment.getMember().getName());
        }
        return dto;
    }
}
