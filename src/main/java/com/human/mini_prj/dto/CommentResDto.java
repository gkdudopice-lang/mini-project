package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResDto {
    private Long commentId;
    private String content;
    private String email;     // 작성자 이메일
    private String name;      // 작성자 이름
    private Long boardId;     // 소속 게시글 번호
    private LocalDateTime regDate; // 작성일
}