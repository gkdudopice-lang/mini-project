package com.human.mini_prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.human.mini_prj.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByRegTimeDesc();
}
