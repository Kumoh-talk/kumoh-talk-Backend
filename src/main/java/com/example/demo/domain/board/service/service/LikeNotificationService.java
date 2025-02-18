package com.example.demo.domain.board.service.service;

import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.notification.repository.NotificationUserRepository;
import com.example.demo.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeNotificationService {
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
    public void deleteLikeNotification(Long id) {
        notificationRepository.deleteByInvokerIdAndInvokerType(id, NotificationType.BOARD_LIKE);
    }
}
