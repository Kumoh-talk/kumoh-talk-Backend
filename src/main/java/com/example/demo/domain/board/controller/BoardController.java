package com.example.demo.domain.board.controller;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.service.usecase.BoardUseCase;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardUseCase boardService;

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
    @PostMapping("/v1/boards")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> save(Long userId,
                                                  @RequestBody @Valid BoardCreateRequest boardCreateRequest)  {

            return ResponseEntity.ok(createSuccessResponse(boardService.saveBoard(userId, boardCreateRequest)));
    }

    @GetMapping("/v1/boards/{boardId}")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> search(@PathVariable Long boardId) {
        return ResponseEntity.ok(createSuccessResponse(boardService.searchSingleBoard(boardId)));
    }


    @AssignUserId
    @PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
    @PatchMapping("/v1/boards")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> update(Long userId,
                                                        @RequestBody @Valid BoardUpdateRequest boardUpdateRequest)  {
        return ResponseEntity.ok(createSuccessResponse(boardService.updateBoard(userId,boardUpdateRequest)));
    }

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
    @DeleteMapping("/v1/boards/{boardId}")
    public ResponseEntity<ResponseBody<Void>> delete(Long userId,@PathVariable Long boardId) {
        boardService.deleteBoard(userId,boardId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_USER') and isAuthenticated()")
    @PostMapping("/v1/boards/{boardId}/like")
    public ResponseEntity<ResponseBody<Void>> like(Long userId,@PathVariable Long boardId) {
        boardService.likeBoard(userId,boardId);
        return ResponseEntity.ok(createSuccessResponse());
    }

//    @GetMapping("/list")
//    public ResponseEntity<BoardPageResponse> findPageList(
//                                                         @RequestParam(defaultValue = "1", required = false) int page,
//                                                         @RequestParam(required = true) Track track,
//                                                         @RequestParam(defaultValue = "DESC", required = false ) PageSort pageSort) {
//
//        return ResponseEntity.ok(boardService.findPageList(page,track,pageSort));
//    }



}
