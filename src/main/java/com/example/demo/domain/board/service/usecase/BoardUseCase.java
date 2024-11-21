package com.example.demo.domain.board.service.usecase;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse;
import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.service.service.BoardCommandService;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.ViewIncreaseService;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.newsletter.strategy.SeminarSummaryEmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardUseCase {
    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;
    private final ViewIncreaseService viewIncreaseService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BoardInfoResponse saveDraftBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = userService.validateUser(userId);
        // 공지사항은 관리자만 작성 가능
        if (boardCreateRequest.getBoardType().equals(BoardType.NOTICE) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ServiceException(ErrorCode.NOT_AUTHORIZED_WRITE_NOTICE);
        }
        return boardCommandService.createDraftBoard(user, boardCreateRequest);
    }

    @Transactional
    public BoardInfoResponse searchSingleBoard(Long boardId) {
        viewIncreaseService.increaseView(boardId);
        return boardQueryService.searchSingleBoard(boardId);
    }

    @Transactional
    public BoardInfoResponse updateBoard(Long userId, BoardUpdateRequest boardUpdateRequest) {
        Board board = boardQueryService.validateBoardForUpdate(boardUpdateRequest, userId);
        BoardInfoResponse boardInfoResponse = boardCommandService.updateBoard(boardUpdateRequest, board);

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
        Board board = boardCommandService.validateBoardForDelete(userId, boardId);
        boardCommandService.removeBoard(board);
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
        userService.validateUser(userId);
        return boardQueryService.findPublishedBoardListByUser(userId,boardType, pageable);
    }
}
