package com.example.demo.domain.board.service.usecase;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.service.service.LikeService;
import com.example.demo.domain.board.service.service.BoardCommandService;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.ViewIncreaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardUseCase {
    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;
    private final ViewIncreaseService viewIncreaseService;
    private final LikeService likeService;

    @Transactional
    public BoardInfoResponse saveBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        return boardCommandService.createBoard(userId, boardCreateRequest);
    }

    @Transactional
    public BoardInfoResponse searchSingleBoard(Long boardId) {
        viewIncreaseService.increaseView(boardId);
        return boardQueryService.findByboardId(boardId);
    }

    @Transactional
    public BoardInfoResponse updateBoard(Long userId, BoardUpdateRequest boardUpdateRequest) {
        return boardCommandService.updateBoard(boardUpdateRequest, userId);
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        boardCommandService.removeBoard(userId, boardId);
    }

    @Transactional
    public void likeBoard(Long userId, Long boardId) {
        likeService.increaseLike(userId, boardId);
    }

}
