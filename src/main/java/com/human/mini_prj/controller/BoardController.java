package com.human.mini_prj.controller;

import com.human.mini_prj.dto.BoardResDto;
import com.human.mini_prj.dto.BoardWriteReqDto;
import com.human.mini_prj.dto.MemberResDto;
import com.human.mini_prj.dto.PageResDto;
import com.human.mini_prj.repository.BoardLikeRepository;
import com.human.mini_prj.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j // log 메세지 작성 하는 용도
@RestController // Restfull API를 지원하는 컨트롤러(직역화/ 역직렬화) 기능을 가지고 있음
@RequiredArgsConstructor // 생서자를 통한 의존성 주입을 자동생성
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService; // 의존성 주입

    // 1. 게시글 전체 조회 (Get: /api/boards)
    @GetMapping // HTTP GET 요청을 처리하며, 주소는 클래스 레벨의 "/api/boards"와 매핑됩니다.
    public ResponseEntity<List<BoardResDto>> boardList() {
        // ResponseEntity: HTTP 상태 코드와 응답 데이터(본문)를 함께 감싸서 보내는 객체입니다.
        // boardService를 통해 전체 게시글 리스트를 가져옵니다.
        List<BoardResDto> list = boardService.getBoardList();
        // 정상적으로 조회되면 HTTP 상태 코드 200(OK)과 함께 리스트 데이터를 반환합니다.
        return ResponseEntity.ok(list);
    }

    // 2. 게시글 상세 조회 (GET: /api/boards/{id})
    @GetMapping("/{id}") // URL 경로에 포함된 변수({id})를 받기 위해 @PathVariable을 사용합니다. (예: /api/boards/1)
    public ResponseEntity<BoardResDto> boardDetail(@PathVariable Long id) {
        try {
            BoardResDto board = boardService.getBoardDetail(id);
            return ResponseEntity.ok(board); // 성공 시 200 OK와 게시글 데이터 반환
        } catch (Exception e) {
            log.error("게시글 상세 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().build(); // 예외 발생 시 400 Bad Request 응답 반환
        }
    }

    // 3. 게시글 작성 (POST: /api/boards)
    @PostMapping // HTTP POST 요청을 처리합니다. 주로 데이터 생성(등록) 시 사용됩니다.
    public ResponseEntity<Boolean> boardWrite(@RequestBody BoardWriteReqDto dto) {
        // @RequestBody: 클라이언트가 보낸 JSON 형태의 요청 데이터를 자바 객체(DTO)로 자동 변환해 줍니다.
        boolean isSuccess = boardService.writeBoard(dto);
        return ResponseEntity.ok(isSuccess); // 성공 여부(true/false)를 담아 200 OK 반환
    }

    // 4. 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> boardUpdate(@PathVariable Long id, @RequestBody BoardWriteReqDto dto) {
        boolean isSuccess = boardService.updateBoard(id, dto);
        return ResponseEntity.ok(isSuccess);
    }

    // 5. 게시글 삭제 (ID만 받도록 수정)
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> boardDelete(@PathVariable Long id) {
        boolean isSuccess = boardService.deleteBoard(id);
        return ResponseEntity.ok(isSuccess);
    }

    // 게시글 목록 페이지네이션 조회
    //
    @GetMapping("/page")
    public ResponseEntity<PageResDto<BoardResDto>> boardListPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(boardService.getBoardPage(page-1, size));
    }

    // 게시물 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long id, @RequestBody Map<String, String> request){
        String email = request.get("email");
        Map<String, Object> result = boardService.toggleLike(id,email);

        return ResponseEntity.ok(result);
    }

    //게시글 검색 + 페이지네이션 조회
    //요청 주소 예시: GET /api/boards/search?keyword=스프링&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<PageResDto<BoardResDto>> searchBoards(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        PageResDto<BoardResDto> result = boardService.searchBoards(keyword, page, size);
        return ResponseEntity.ok(result);
    }

    // 내가 쓴 글 목록 조회
    // 요청 주소 예시: GET /api/boards/my-boards?email=user21@test.com&page=0&size=10
    @GetMapping("/my_boards")
    public ResponseEntity<PageResDto<BoardResDto>> getMyBoards(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        PageResDto<BoardResDto> result = boardService.getMyBoards(email, page, size);
        return ResponseEntity.ok(result);
    }




}
