package com.example.demo.domain.board.service;

import com.example.demo.domain.board.domain.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.response.BoardInfoResponse;
import com.example.demo.domain.board.service.component.LikeComponent;
import com.example.demo.domain.board.service.component.command.BoardCommand;
import com.example.demo.domain.board.service.component.query.BoardQuery;
import com.example.demo.domain.board.service.component.command.ViewIncreaseCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl {
    private final BoardCommand boardCommand;
    private final BoardQuery boardQuery;
    private final ViewIncreaseCommand viewIncreaseCommand;
    private final LikeComponent likeComponent;

    @Transactional
    public BoardInfoResponse saveBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        return boardCommand.createBoard(userId, boardCreateRequest);
    }

    @Transactional
    public BoardInfoResponse searchSingleBoard(Long boardId) {
        viewIncreaseCommand.increaseView(boardId);
        return boardQuery.findByboardId(boardId);
    }

    @Transactional
    public BoardInfoResponse updateBoard(Long userId, BoardUpdateRequest boardUpdateRequest) {
        return boardCommand.updateBoard(boardUpdateRequest, userId);
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        boardCommand.removeBoard(userId, boardId);
    }

    @Transactional
    public void likeBoard(Long userId, Long boardId) {
        likeComponent.increaseLike(userId, boardId);
    }

}
