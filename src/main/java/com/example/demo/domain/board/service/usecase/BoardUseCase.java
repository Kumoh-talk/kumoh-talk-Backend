package com.example.demo.domain.board.service.usecase;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
import com.example.demo.domain.board.domain.dto.vo.Tag;
import com.example.demo.domain.board.service.service.BoardCommandService;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.ViewIncreaseService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardUseCase {
    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;
    private final ViewIncreaseService viewIncreaseService;

    @Transactional
    public BoardInfoResponse saveDraftSeminarBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        return boardCommandService.createBoard(userId, boardCreateRequest, Tag.seminar);
    }

    @Transactional
    public BoardInfoResponse saveDraftNoticeBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        return boardCommandService.createBoard(userId, boardCreateRequest,Tag.notice);
    }

    @Transactional
    public BoardInfoResponse searchSingleBoard(Long boardId) {
        viewIncreaseService.increaseView(boardId);
        return boardQueryService.findByboardId(boardId);
    }

    public BoardInfoResponse updateBoard(Long userId, BoardUpdateRequest boardUpdateRequest) {
        return boardCommandService.updateBoard(boardUpdateRequest, userId);
    }

    public void deleteBoard(Long userId, Long boardId) {
        boardCommandService.removeBoard(userId, boardId);
    }

    @Transactional(readOnly = true)
	public BoardPageResponse findBoardList(Pageable pageable) {
        return boardQueryService.findBoardPageList(pageable);
	}



}
