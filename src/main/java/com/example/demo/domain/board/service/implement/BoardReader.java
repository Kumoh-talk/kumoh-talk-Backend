package com.example.demo.domain.board.service.implement;

import java.util.Optional;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.application.board.dto.request.BoardUpdateRequest;
import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.application.board.dto.response.DraftBoardTitleResponse;
import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtAuthentication;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardReader {
    private final BoardJpaRepository boardJpaRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public Optional<BoardInfo> searchSingleBoard(Long boardId) {
        return boardRepository.findBoardInfo(boardId);
    }

    @Transactional(readOnly = true)
	public GlobalPageResponse<BoardTitleInfoResponse> findBoardPageList(BoardType boardType, Pageable pageable) {
        Page<BoardTitleInfoResponse> boardByPage = boardJpaRepository.findBoardByPage(boardType,pageable);
        return GlobalPageResponse.create(boardByPage);
    }


    @Transactional(readOnly = true)
    public Board validateBoard(Long boardId) {
        return boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Board validateBoardForUpdate(BoardUpdateRequest boardUpdateRequest, Long userId) {
        Board board = validateBoard(boardUpdateRequest.getId());
        validateUserEqualBoardUser(userId, board);
        return board;
    }

    private void validateUserEqualBoardUser(Long userId, Board board) {
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
    }

    public GlobalPageResponse<DraftBoardTitleResponse> findDraftBoardPageList(Long userId,Pageable pageable) {
        return GlobalPageResponse.create(boardJpaRepository.findDraftBoardByPage(userId,pageable));
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfoResponse> findPublishedBoardListByUser(Long userId,
        BoardType boardType,
        Pageable pageable) {
        return GlobalPageResponse.create(boardJpaRepository.findPublishedBoardListByUser(userId,boardType,pageable));
    }
}
