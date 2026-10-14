package com.human.mini_prj.controller;

import com.human.mini_prj.dto.CommentReqDto;
import com.human.mini_prj.dto.CommentResDto;
import com.human.mini_prj.entity.Comment;
import com.human.mini_prj.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<Boolean> commemtWrite(@RequestBody CommentReqDto dto){
        boolean isSuccess = commentService.writeComment(dto);
        return ResponseEntity.ok(isSuccess);
    }

    // 2. 특정 게시글의 댓글 목록 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentResDto>> commentList(@PathVariable Long boardId){
        List<CommentResDto> list = commentService.getCommentList(boardId);
        return ResponseEntity.ok(list);
    }

    // 3. 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Boolean> commentUpdate(@PathVariable Long commentId, @RequestBody CommentReqDto dto){
        boolean isSuccess = commentService.updateComment(commentId, dto);
        return ResponseEntity.ok(isSuccess);
    }

    // 4. 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> commentDelete(@PathVariable Long commentId){
        boolean isSuccess = commentService.deleteComment(commentId);
        return ResponseEntity.ok(isSuccess);
    }
}
