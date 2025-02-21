package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class LikeNotificationService {
    private final NotificationRepository notificationRepository;

    @Async
    @Transactional
    public CompletableFuture<Notification> saveLikeNotification(Like like) {
        Notification notification = Notification.fromLikeEntity(like, NotificationType.BOARD_LIKE);
        if (!like.getBoard().getUser().getId().equals(like.getUser().getId())) {
            notification.getNotificationUserList().add(NotificationUser.from(notification, like.getBoard().getUser()));
            return CompletableFuture.completedFuture(notificationRepository.save(notification));
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional
    public void deleteLikeNotification(Long id) {
        notificationRepository.deleteByInvokerIdAndInvokerType(id, NotificationType.BOARD_LIKE);
    }
}
