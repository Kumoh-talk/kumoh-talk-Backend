package com.example.demo.domain.board.service.usecase;

import com.example.demo.application.board.dto.request.BoardUpdateRequest;
import com.example.demo.application.board.dto.response.BoardInfoResponse;
import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.application.board.dto.response.DraftBoardTitleResponse;
import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardCore;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.implement.BoardCategoryWriter;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.domain.board.service.implement.BoardWriter;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.view.ViewIncreaseService;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.newsletter.strategy.SeminarSummaryEmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {
    private final BoardWriter boardWriter;
    private final BoardQueryService boardQueryService;
    private final ViewIncreaseService viewIncreaseService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserReader userReader;
    private final BoardCategoryWriter boardCategoryWriter;

    public BoardService(BoardWriter boardWriter,
        BoardQueryService boardQueryService,
        @Qualifier("viewBulkUpdateService") ViewIncreaseService viewIncreaseService,
        UserReader userReader,
        ApplicationEventPublisher eventPublisher, BoardCategoryWriter boardCategoryWriter) {
        this.boardWriter = boardWriter;
        this.boardQueryService = boardQueryService;
        this.viewIncreaseService = viewIncreaseService;
        this.userReader = userReader;
        this.eventPublisher = eventPublisher;
		this.boardCategoryWriter = boardCategoryWriter;
	}

    @Transactional
    public BoardInfo saveDraftBoard(Long userId, BoardCore boardCore , BoardCategoryNames boardCategoryNames) {
        UserTarget userTarget = userReader.findUser(userId)
            .orElseThrow(()-> new ServiceException(ErrorCode.USER_NOT_FOUND));

        // 공지사항은 관리자만 작성 가능
        if (isAdminAndNotice(boardCore, userTarget)) {
            throw new ServiceException(ErrorCode.NOT_AUTHORIZED_WRITE_NOTICE);
        }
        BoardInfo draftBoard = boardWriter.createDraftBoard(userTarget, boardCore.draftBoard());

        boardCategoryWriter.saveCategoryNames(draftBoard, boardCategoryNames);
        draftBoard.setBoardCategoryNames(boardCategoryNames);

        return draftBoard;

    }

    private boolean isAdminAndNotice(BoardCore boardCore, UserTarget userTarget) {
        return boardCore.getBoardType().equals(BoardType.NOTICE) && !userTarget.getUserRole().equals(Role.ROLE_ADMIN);
    }

    public BoardInfoResponse searchSingleBoard(Long boardId) {
        BoardInfoResponse returnResponse = boardQueryService.searchSingleBoard(boardId);
        viewIncreaseService.increaseView(boardId);
        return returnResponse;
    }

    @Transactional
    public BoardInfoResponse updateBoard(Long userId, BoardUpdateRequest boardUpdateRequest) {
        Board board = boardQueryService.validateBoardForUpdate(boardUpdateRequest, userId);
        BoardInfoResponse boardInfoResponse = boardWriter.updateBoard(boardUpdateRequest, board);

        // 게시 상태로 변경이면 뉴스레터 전송
        if (boardUpdateRequest.getIsPublished() && board.getBoardType().equals(BoardType.SEMINAR) && board.getStatus().equals(Status.DRAFT)) {
            eventPublisher.publishEvent(EmailNotificationEvent.create(
                    EntireBoardType.SEMINAR_SUMMARY,
                    SeminarSummaryEmailDeliveryStrategy.create(board)
            ));
        }

        return boardInfoResponse;
    }

    @Transactional
    public void deleteBoard(Long userId, Long boardId) {
        Board board = boardWriter.validateBoardForDelete(userId, boardId);
        boardWriter.removeBoard(board);
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfoResponse> findBoardList(BoardType boardType , Pageable pageable) {
        return boardQueryService.findBoardPageList(boardType,pageable);
    }

    public GlobalPageResponse<DraftBoardTitleResponse> findDraftBoardList(Long userId, Pageable pageable) {
        return boardQueryService.findDraftBoardPageList(userId,pageable);
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfoResponse> findMyBoardPageList(Long userId,BoardType boardType, Pageable pageable) {
        userReader.findUser(userId)
            .orElseThrow(()-> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return boardQueryService.findPublishedBoardListByUser(userId,boardType, pageable);
    }
}
