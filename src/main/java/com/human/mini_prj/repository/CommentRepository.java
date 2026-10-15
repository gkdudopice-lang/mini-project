package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardIdOrderByRegDateDesc(Long boardId);
    // CommentRepository에 추가
    Page<Comment> findByMemberEmail(String email, Pageable pageable);
// 또는 언더바가 필요하다면 findByMember_Email(String email, Pageable pageable);
    long countByBoardId(Long boardId);

}
