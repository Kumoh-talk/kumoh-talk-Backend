package com.example.demo.application.board.controller;


import static com.example.demo.global.base.dto.ResponseUtil.*;

import com.example.demo.application.board.api.BoardApi;
import com.example.demo.application.board.dto.request.BoardCreateRequest;
import com.example.demo.application.board.dto.request.BoardUpdateRequest;
import com.example.demo.application.board.dto.response.BoardInfoResponse;
import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.application.board.dto.response.DraftBoardTitleResponse;
import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.usecase.BoardService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import com.example.demo.global.base.dto.page.GlobalPageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController implements BoardApi {
    private final BoardService boardUsecase;

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
    @PostMapping("/v1/boards")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> saveDraftSeminar(Long userId,
        @RequestBody @Valid BoardCreateRequest boardCreateRequest) {

        return ResponseEntity.ok(createSuccessResponse(
            BoardInfoResponse.of(
                boardUsecase.saveDraftBoard(
            userId,
            boardCreateRequest.toBoardContent(),
            boardCreateRequest.toBoardCategoryNames()))));
    }

    @AssignUserId
    @GetMapping("/v1/boards/{boardId}")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> search(Long userId, @PathVariable Long boardId) {
        BoardInfo boardInfo = boardUsecase.searchSingleBoard(userId, boardId);
        return ResponseEntity.ok(createSuccessResponse(
            BoardInfoResponse.of(boardInfo)));
    }


    @AssignUserId
    @PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
    @PatchMapping("/v1/boards")
    public ResponseEntity<ResponseBody<BoardInfoResponse>> update(Long userId,
                                                        @RequestBody @Valid BoardUpdateRequest boardUpdateRequest)  {
        return ResponseEntity.ok(createSuccessResponse(boardUsecase.updateBoard(userId,boardUpdateRequest)));
    }

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
    @DeleteMapping("/v1/boards/{boardId}")
    public ResponseEntity<ResponseBody<Void>> delete(Long userId,@PathVariable Long boardId) {
        boardUsecase.deleteBoard(userId,boardId);
        return ResponseEntity.ok(createSuccessResponse());
    }


    @GetMapping("/v1/boards")
    public ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfoResponse>>> findBoardPageList(
        @RequestParam(defaultValue = "SEMINAR") BoardType boardType,
        @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(boardUsecase.findBoardList(boardType,pageable)));
    }

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
    @GetMapping("/v1/boards/draft")
    public ResponseEntity<ResponseBody<GlobalPageResponse<DraftBoardTitleResponse>>> findDraftBoardPageList(
        Long userId,
        @PageableDefault(page=0, size=10,sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(boardUsecase.findDraftBoardList(userId,pageable)));
    }

    @AssignUserId
    @PreAuthorize("hasRole('ROLE_SEMINAR_WRITER') and isAuthenticated()")
    @GetMapping("/v1/boards/me")
    public ResponseEntity<ResponseBody<GlobalPageResponse<BoardTitleInfoResponse>>> findMyBoardPageList(
        Long userId,
        @RequestParam(defaultValue = "SEMINAR") BoardType boardType,
        @PageableDefault(page=0, size=10,sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(createSuccessResponse(boardUsecase.findMyBoardPageList(userId,boardType,pageable)));
    }

}
