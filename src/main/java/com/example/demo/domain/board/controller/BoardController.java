package com.example.demo.domain.board.controller;


import com.example.demo.domain.auth.domain.UserContext;
import com.example.demo.domain.board.domain.request.BoardRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.service.BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController { // TODO : princapal null 값 반환 확인 후 user null 감지 로직 추가 고민해야함

    private final BoardService boardService;


    @PostMapping("/save")
    public ResponseEntity<BoardInfoResponse> save(@AuthenticationPrincipal UserContext user,
                                                  @ModelAttribute @Valid BoardRequest boardRequest) throws IOException {

            return ResponseEntity.ok(boardService.save(boardRequest, user.getId()));
    }
    @GetMapping("/search/{boardId}") //TODO : @PathValidation validation 처리 한번 생각해봐야함
    public ResponseEntity<BoardInfoResponse> search(@PathVariable Long boardId) throws IOException {
        return ResponseEntity.ok(boardService.findById(boardId));
    }
    @PostMapping("/update/{boardId}")
    public ResponseEntity<BoardInfoResponse> update(@AuthenticationPrincipal UserContext user,
                                                        @RequestBody @Valid BoardRequest boardRequest,
                                                        @PathVariable Long boardId) throws IOException {
        return ResponseEntity.ok(boardService.update(boardRequest,user.getId(),boardId));
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
