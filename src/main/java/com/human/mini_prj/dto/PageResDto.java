package com.human.mini_prj.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/*
- 페이지 네이션 공동 응답 DTO
- 회원 목록, 게시글 목록 등 어떤 목록이든 재사용할 수 있게 제네릭(T)로 만듦
- Page 객체를 그대로 반환하지 않고, 프론트에 필요한 정보만 추려서 내려줌
 */

@Getter
public class PageResDto<T> { // T는 기본타입은 못옴, 참조 타입만 올 수 있음
    private final List<T> content; // 현재 페이지의 데이터 목록
    private final int page; // 현재 페이지 번호(0부터 시작)
    private final int size; // 한 페이지당 데이터 개수
    private final long totalElements; // 전체 데이터 개수
    private final int totalPages; // 전체 페이지 수
    private final boolean first; // 첫 페이지 여부
    private final boolean last; // 마지막 페이지 여부

    public PageResDto(Page<T> page){
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page. getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
    }


}
