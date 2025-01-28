package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.Repository.LikeRepository;
import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.notification.repository.NotificationUserRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeService {
    private final LikeNotificationService likeNotificationService;
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void increaseLike(Long userId, Long boardId) {
        Board board = (Board) boardRepository.findByIdWithUser(boardId).orElseThrow
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
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
    }

    private Board validateBoard(Long boardId) {
        return boardRepository.findById(boardId)
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
        likeNotificationService.deleteLikeNotification(like);
    }


    @Component
    @RequiredArgsConstructor
    static class LikeNotificationService {
        private final NotificationRepository notificationRepository;
        private final NotificationUserRepository notificationUserRepository;

        @Async
        @Transactional
        public void saveLikeNotification(Like like) {
            Notification notification = Notification.fromLikeEntity(like, NotificationType.BOARD_LIKE);
            if (!like.getBoard().getUser().getId().equals(like.getUser().getId())) {
                User user = like.getBoard().getUser();
                Notification saved = notificationRepository.save(notification);
                notificationUserRepository.save(NotificationUser.from(saved, user));
            }

        }

        @Async
        @Transactional
        public void deleteLikeNotification(Like like) {
            notificationRepository.deleteByInvokerIdAndInvokerType(like.getId(), NotificationType.BOARD_LIKE);
        }
    }
}
