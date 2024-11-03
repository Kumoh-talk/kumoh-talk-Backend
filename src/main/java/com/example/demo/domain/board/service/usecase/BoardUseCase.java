package com.example.demo.domain.board.service.usecase;

import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.response.BoardInfoResponse;
import com.example.demo.domain.board.domain.dto.response.BoardPageResponse;
import com.example.demo.domain.board.domain.dto.vo.Tag;
import com.example.demo.domain.board.service.service.BoardCommandService;
import com.example.demo.domain.board.service.service.BoardQueryService;
import com.example.demo.domain.board.service.service.ViewIncreaseService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;

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
    private final UserService userService;

    @Transactional
    public BoardInfoResponse saveDraftBoard(Long userId, BoardCreateRequest boardCreateRequest) {
        User user = userService.validateUser(userId);
        // 공지사항은 관리자만 작성 가능
        if(boardCreateRequest.getTag().equals(Tag.notice) && !user.getRole().equals(Role.ROLE_ADMIN)){
            throw new ServiceException(ErrorCode.NOT_AUTHORIZED_WRITE_NOTICE);
        }
        return boardCommandService.createDraftBoard(user, boardCreateRequest);
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
