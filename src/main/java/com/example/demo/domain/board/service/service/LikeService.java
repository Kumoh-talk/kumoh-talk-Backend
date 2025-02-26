package com.example.demo.domain.board.service.service;

import com.example.demo.application.board.dto.response.BoardTitleInfoResponse;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.infra.board.Repository.LikeRepository;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final LikeNotificationService likeNotificationService;

    @Transactional
    public void increaseLike(Long userId, Long boardId) {
        Board board = (Board) boardJpaRepository.findByIdWithUser(boardId).orElseThrow
                (() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        User user = validateUser(userId);
        if (likeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new ServiceException(ErrorCode.USER_ALREADY_LIKE_BOARD);
        }
        Like like = new Like(user, board);
        likeRepository.save(like);
        likeNotificationService.saveLikeNotification(like);
    }


    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfoResponse> getLikes(Long userId, Pageable pageable) {
        return GlobalPageResponse.create(likeRepository.findBoardsByUserId(userId, pageable));
    }

    private User validateUser(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
    }

    private Board validateBoard(Long boardId) {
        return boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void decreaseLike(Long userId, Long boardId) {
        Board board = validateBoard(boardId);
        User user = validateUser(userId);
        Like like = likeRepository.findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_LIKE_BOARD));
        board.getLikes().remove(like);
        user.getLikes().remove(like);
        likeRepository.delete(like);
    }
}
