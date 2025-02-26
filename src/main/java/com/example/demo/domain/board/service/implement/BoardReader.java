package com.example.demo.domain.board.service.implement;

import java.util.Optional;

import com.example.demo.domain.base.page.GlobalPageableDto;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.repository.BoardRepository;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.application.board.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.DraftBoardTitle;
import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BoardReader {
    private final BoardJpaRepository boardJpaRepository;
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public Optional<BoardInfo> searchSingleBoard(Long boardId) {
        return boardRepository.findBoardInfo(boardId);
    }

    @Transactional(readOnly = true)
    public GlobalPageableDto<BoardTitleInfo> findPublishedBoardPageList(BoardType boardType, GlobalPageableDto pageableDto) {
        pageableDto.setPage(boardRepository.findBoardTitleInfoPage(boardType, pageableDto));
        return pageableDto;
    }


    @Transactional(readOnly = true)
    public Board validateBoard(Long boardId) {
        return boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Board validateBoardForUpdate(BoardUpdateRequest boardUpdateRequest, Long userId) {
        Board board = validateBoard(boardUpdateRequest.id());
        validateUserEqualBoardUser(userId, board);
        return board;
    }

    private void validateUserEqualBoardUser(Long userId, Board board) {
        if(!board.getUser().getId().equals(userId)) {
            throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
        }
    }

    public GlobalPageableDto<DraftBoardTitle> findDraftBoardPageList(Long userId, GlobalPageableDto pageableDto) {
        pageableDto.setPage(boardRepository.findDraftBoardByPage(userId, pageableDto));
        return pageableDto;
    }

    @Transactional(readOnly = true)
    public GlobalPageableDto<BoardTitleInfo> findPublishedBoardListByUser(Long userId,
        BoardType boardType,
        GlobalPageableDto pageableDto) {
        pageableDto.setPage(boardJpaRepository.findPublishedBoardListByUser(userId,boardType,pageableDto.getPageable()));
        return pageableDto;
    }
}
