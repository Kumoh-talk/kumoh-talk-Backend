package com.example.demo.domain.board.service.service;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.DraftBoardTitle;
import com.example.demo.domain.board.service.implement.BoardCategoryWriter;
import com.example.demo.domain.board.service.implement.BoardReader;
import com.example.demo.domain.board.service.implement.BoardValidator;
import com.example.demo.domain.board.service.implement.BoardWriter;
import com.example.demo.domain.board.service.view.implement.ViewCounter;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.newsletter.strategy.SeminarSummaryEmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardWriter boardWriter;
    private final BoardReader boardReader;
    private final ViewCounter viewCounter;
    private final ApplicationEventPublisher eventPublisher;
    private final UserReader userReader;
    private final BoardCategoryWriter boardCategoryWriter;
    private final BoardValidator boardValidator;



    @Transactional
    public BoardInfo saveDraftBoard(Long userId, BoardContent boardContent, BoardCategoryNames boardCategoryNames) {
        UserTarget userTarget = userReader.findUser(userId)
            .orElseThrow(()-> new ServiceException(ErrorCode.USER_NOT_FOUND));

        // 공지사항은 관리자만 작성 가능
        if (isAdminAndNotice(boardContent, userTarget)) {
            throw new ServiceException(ErrorCode.NOT_AUTHORIZED_WRITE_NOTICE);
        }
        BoardInfo draftBoard = boardWriter.createDraftBoard(userTarget, boardContent.draftBoard());

        boardCategoryWriter.saveCategoryNames(draftBoard, boardCategoryNames);
        return draftBoard.setBoardCategoryNames(boardCategoryNames);
    }

    private boolean isAdminAndNotice(BoardContent boardContent, UserTarget userTarget) {
        return boardContent.getBoardType().equals(BoardType.NOTICE) && !userTarget.getUserRole().equals(Role.ROLE_ADMIN);
    }

    public BoardInfo searchSingleBoard(Long userId, Long boardId) {
        BoardInfo boardInfo = boardReader.searchSingleBoard(boardId)
            .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        boardValidator.validateDraftBoardIsOwner(userId, boardInfo);

        viewCounter.increaseView(boardId);
        return boardInfo;
    }

    @Transactional
    public BoardInfo updateBoard(Long userId,Long boardId, BoardContent updateBoardContent, BoardCategoryNames updateBoardCategoryNames, Boolean isPublished) {
        BoardInfo savedBoardInfo = boardReader.searchSingleBoard(boardId)
            .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        boardValidator.validateUserEqualBoardUser(userId, savedBoardInfo);

        BoardInfo contentModifiedBoardInfo = boardWriter.modifyBoarContent(savedBoardInfo,updateBoardContent,isPublished);
        BoardInfo modifiedBoardInfo = boardCategoryWriter.modifyBoardCategories(contentModifiedBoardInfo,updateBoardCategoryNames);

        // 세미나 게시물이 게시 상태로 변경이면 뉴스레터 전송 TODO : 추후 뉴스레터 전송 Implement Layer로 전환 필요
        if (isSeminarBoardModifiedToPublished(isPublished,savedBoardInfo.getBoardContent(), modifiedBoardInfo.getBoardContent())) {
            eventPublisher.publishEvent(EmailNotificationEvent.create(
                    EntireBoardType.SEMINAR_SUMMARY,
                    SeminarSummaryEmailDeliveryStrategy.create(modifiedBoardInfo)
            ));
        }

        return modifiedBoardInfo;
    }

    private Boolean isSeminarBoardModifiedToPublished(Boolean isPublished,BoardContent previousBoardContent, BoardContent modifiedBoardContent) {
        return modifiedBoardContent.getBoardType().equals(BoardType.SEMINAR)
            && previousBoardContent.getBoardStatus().equals(Status.DRAFT)
            && isPublished;
    }


    public void deleteBoard(Long userId, Long boardId) {
        BoardInfo savedBoardInfo = boardReader.searchSingleBoard(boardId)
            .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        boardValidator.validateUserEqualBoardUser(userId, savedBoardInfo);

        boardCategoryWriter.removeBoardCategories(savedBoardInfo);
        boardWriter.removeBoardContent(savedBoardInfo);
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfo> findPublishedBoardList(BoardType boardType , Pageable pageable) {
        return boardReader.findPublishedBoardPageList(boardType,pageable);
    }

    public GlobalPageResponse<DraftBoardTitle> findDraftBoardList(Long userId, Pageable pageable) {
        return boardReader.findDraftBoardPageList(userId,pageable);
    }

    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfo> findMyBoardPageList(Long userId,BoardType boardType, Pageable pageable) {
        userReader.findUser(userId)
            .orElseThrow(()-> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return boardReader.findPublishedBoardListByUser(userId,boardType, pageable);
    }

}
