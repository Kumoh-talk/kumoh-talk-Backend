package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse;
import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
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
    public BoardInfoResponse searchSingleBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        validateBoardStatus(board); // 임시저장 게시물은 작성자만 조회 가능하기 때문에 작성자인지 확인
        validateReportedBoard(board);  //TODO : [Board]report 기능 추가 시 로직 변경


        return BoardInfoResponse.from(
                board,
                board.getUser().getNickname(),
                boardRepository.countLikesByBoardId(boardId),
                boardRepository.findCategoryNameByBoardId(boardId));
    }

    @Transactional(readOnly = true)
	public GlobalPageResponse<BoardTitleInfoResponse> findBoardPageList(BoardType boardType, Pageable pageable) {
        Page<BoardTitleInfoResponse> boardByPage = boardRepository.findBoardByPage(boardType,pageable);
        return GlobalPageResponse.create(boardByPage);
    }

    private void validateReportedBoard(Board board) { //TODO : [Board]report 기능 추가 시 로직 추가
    }

    // 임시저장 게시물은 작성자만 조회 가능하기 때문에 작성자인지 확인
    private void validateBoardStatus(Board board) {
        if(board.getStatus().equals(Status.DRAFT)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.isAuthenticated() && authentication instanceof JwtAuthentication) {
                Long userId = (Long) authentication.getPrincipal();
                if(!board.getUser().getId().equals(userId)) {
                    throw new ServiceException(ErrorCode.DRAFT_NOT_ACCESS_USER);
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

    public GlobalPageResponse<DraftBoardTitleResponse> findDraftBoardPageList(Long userId,Pageable pageable) {
        return GlobalPageResponse.create(boardRepository.findDraftBoardByPage(userId,pageable));
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfoResponse> findPublishedBoardListByUser(Long userId,
        BoardType boardType,
        Pageable pageable) {
        return GlobalPageResponse.create(boardRepository.findPublishedBoardListByUser(userId,boardType,pageable));
    }
}
