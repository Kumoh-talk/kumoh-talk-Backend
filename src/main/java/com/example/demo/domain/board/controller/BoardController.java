package com.example.demo.domain.board.controller;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.board.domain.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.service.BoardService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController { // TODO : princapal null 값 반환 확인 후 user null 감지 로직 추가 고민해야함

    private final BoardService boardService;

    @AssignUserId
    @PostMapping
    public ResponseEntity<ResponseBody<BoardInfoResponse>> save(Long userId,
                                                  @RequestBody @Valid BoardCreateRequest boardCreateRequest) throws IOException {

            return ResponseEntity.ok(createSuccessResponse(boardService.boardCreate(userId, boardCreateRequest)));
    }

    @GetMapping("/search/{boardId}") //TODO : @PathValidation validation 처리 한번 생각해봐야함
    public ResponseEntity<ResponseBody<BoardInfoResponse>> search(@PathVariable Long boardId) throws IOException {
        return ResponseEntity.ok(createSuccessResponse(boardService.findById(boardId)));
    }

    @AssignUserId
    @PostMapping("/update/{boardId}")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> update(Long userId,
                                                        @RequestBody @Valid BoardCreateRequest boardCreateRequest,
                                                        @PathVariable Long boardId) throws IOException {
        return ResponseEntity.ok(createSuccessResponse(boardService.update(boardCreateRequest,userId,boardId)));
    }

//
//    @PatchMapping("/delete/{postId}")
//    public ResponseEntity delete(@AuthenticationPrincipal UserPrincipal user,@PathVariable Long postId) {
//        boardService.remove(postId, user.getUsername());
//        return ResponseEntity.ok().build();
//    }

//    @GetMapping("/list")
//    public ResponseEntity<BoardPageResponse> findPageList(
//                                                         @RequestParam(defaultValue = "1", required = false) int page,
//                                                         @RequestParam(required = true) Track track,
//                                                         @RequestParam(defaultValue = "DESC", required = false ) PageSort pageSort) {
//
//        return ResponseEntity.ok(boardService.findPageList(page,track,pageSort));
//    }



}
