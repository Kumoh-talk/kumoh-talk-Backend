package com.example.demo.domain.notification.implement;

import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.board.service.implement.BoardReader;
import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class LikeNotificationWriter {
    private final NotificationRepository notificationRepository;
    private final UserReader userReader;
    private final BoardReader boardReader;

    @Async
    @Transactional
    public CompletableFuture<NotificationInfo> saveLikeNotification(LikeInfo likeInfo) {
        BoardInfo boardInfo = boardReader.searchSingleBoard(likeInfo.getBoardId())
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        UserTarget userTarget = userReader.findUserTarget(likeInfo.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        likeInfo.setUserNickname(userTarget.getNickName());
        if (!boardInfo.getUserTarget().getUserId().equals(likeInfo.getUserId())) {
            return CompletableFuture.completedFuture(notificationRepository.postLikeNotification(likeInfo, NotificationType.BOARD_LIKE, boardInfo.getUserTarget().getUserId()));
        }
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public void deleteLikeNotification(Long likeId) {
        notificationRepository.deleteByInvokerIdAndInvokerType(likeId, NotificationType.BOARD_LIKE);
    }

    @Transactional
    public void deleteAllLikeNotification(List<Long> likeIdList) {
        notificationRepository.deleteAllByInvokerIdAndInvokerType(likeIdList, NotificationType.BOARD_LIKE);
    }
}
