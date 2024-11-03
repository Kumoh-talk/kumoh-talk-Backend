package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.dto.vo.Status;
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
public class BoardQueryService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public BoardInfoResponse findByboardId(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        validateBoardStatus(board); //TODO : [Board]조회수 증가 로직 신고 글 안보임 수정 사항이 많음
        validateReportedBoard(board);  //TODO : [Board]report 기능 추가 시 로직 변경

        return BoardInfoResponse.from(
                board,
                board.getUser().getNickname(),
                boardRepository.countViewsByBoardId(boardId),
                boardRepository.countLikesByBoardId(boardId),
                boardRepository.findCategoryNameByBoardId(boardId));
    }

    @Transactional(readOnly = true)
	public BoardPageResponse findBoardPageList(Pageable pageable) {
        Page<BoardTitleInfoResponse> boardByPage = boardRepository.findBoardByPage(pageable);
        return BoardPageResponse.from(boardByPage);
    }

    private void validateReportedBoard(Board board) { //TODO : [Board]report 기능 추가 시 로직 추가
    }

    private void validateBoardStatus(Board board) {
        if(board.getStatus().equals(Status.DRAFT)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.isAuthenticated() && authentication instanceof JwtAuthentication) {
                Long userId = (Long) authentication.getPrincipal();
                if(!board.getUser().getId().equals(userId)) {
                    throw new ServiceException(ErrorCode.BOARD_NOT_FOUND);
                }
            } else {
                throw new ServiceException(ErrorCode.NOT_ACCESS_USER);
            }

        }
    }

    @Transactional(readOnly = true)
    public Board validateBoard(Long boardId) {
        return boardRepository.findById(boardId)
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

}
