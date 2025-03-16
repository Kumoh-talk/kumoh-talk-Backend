package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.board.service.implement.BoardValidator;
import com.example.demo.domain.board.service.implement.LikeHandler;
import com.example.demo.domain.notification.implement.LikeNotificationWriter;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeHandler likeHandler;
    private final UserReader userReader;
    private final BoardValidator boardValidator;

    private final LikeNotificationWriter likeNotificationWriter;

    public void likeBoard(Long userId, Long boardId) {
        boardValidator.validateExistBoard(boardId);
        userReader.findUser(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
        likeHandler.validateExistLike(boardId, userId);
        LikeInfo likeInfo = likeHandler.increaseLike(userId, boardId);
        likeNotificationWriter.saveLikeNotification(likeInfo);
    }

    public GlobalPageResponse<BoardTitleInfo> getLikes(Long userId, Pageable pageable) {
        return likeHandler.getLikes(userId, pageable);
    }

    public void unlikeBoard(Long userId, Long boardId) {
        boardValidator.validateExistBoard(boardId);
        userReader.findUser(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
        Long likeId = likeHandler.findLikeId(boardId, userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_LIKE_BOARD));

        likeHandler.decreaseLike(likeId, userId, boardId);
        likeNotificationWriter.deleteLikeNotification(likeId);
    }
}
