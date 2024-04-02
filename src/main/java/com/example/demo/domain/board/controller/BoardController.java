package com.example.demo.domain.board.controller;


import com.example.demo.domain.auth.domain.UserPrincipal;
import com.example.demo.domain.board.domain.request.BoardRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.service.BoardService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @PostMapping("/save")
    public ResponseEntity<BoardInfoResponse> save(
            @AuthenticationPrincipal UserPrincipal user,
            @ModelAttribute @Valid BoardRequest boardRequest) throws IOException {
        CheckAuthentication(user);

            return ResponseEntity.ok(boardService.save(boardRequest, user.getId()));
    }

    @PostMapping("/update/{postId}")
    public ResponseEntity<BoardInfoResponse> update(@AuthenticationPrincipal UserPrincipal user,
                                                        @ModelAttribute @Valid BoardRequest boardRequest,
                                                        @PathVariable Long postId) throws IOException {
        CheckAuthentication(user);
        return ResponseEntity.ok(boardService.update(boardRequest,user.getName(),postId));
    }
    @GetMapping("/search/{postId}")
    public ResponseEntity<BoardInfoResponse> search(@AuthenticationPrincipal UserPrincipal user,
                                                        @PathVariable Long postId) throws IOException {
        CheckAuthentication(user);
        return ResponseEntity.ok(boardService.findById(postId,user.getName()));
    }

    @PatchMapping("/delete/{postId}")
    public ResponseEntity delete(@AuthenticationPrincipal UserPrincipal user,@PathVariable Long postId) {
        CheckAuthentication(user);
        boardService.remove(postId, user.getUsername());
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/list")
//    public ResponseEntity<BoardPageResponse> findPageList(
//                                                         @RequestParam(defaultValue = "1", required = false) int page,
//                                                         @RequestParam(required = true) Track track,
//                                                         @RequestParam(defaultValue = "DESC", required = false ) PageSort pageSort) {
//
//        return ResponseEntity.ok(boardService.findPageList(page,track,pageSort));
//    }

    private void CheckAuthentication(UserPrincipal user) {
        if(user == null){
            throw new ServiceException(ErrorCode.NEED_AUTHORIZED);
        }
    }

}
