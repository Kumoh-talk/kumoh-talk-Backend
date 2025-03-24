package com.example.demo.infra.notification.repository.impl;

import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.infra.notification.entity.Notification;
import com.example.demo.infra.notification.repository.jpa.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public int deleteAllZeroUsersNotifications() {
        return notificationJpaRepository.deleteAllZeroUsersNotifications();
    }

    @Override
    @Async
    @Transactional
    public void deleteByInvokerIdAndInvokerType(Long invokerId, NotificationType invokerType) {
        notificationJpaRepository.deleteByInvokerIdAndInvokerType(invokerId, invokerType);
    }

    @Override
    @Async
    @Transactional
    public void deleteAllByInvokerIdAndInvokerType(List<Long> likeIdList, NotificationType invokerType) {
        notificationJpaRepository.deleteAllByInvokerIdInAndInvokerType(likeIdList, invokerType);
    }

    @Override
    public NotificationInfo postCommentNotification(CommentInfo commentInfo, NotificationType notificationType, Set<Long> notificationUserIdSet) {
        return notificationJpaRepository.save(
                        Notification.fromCommentInfo(commentInfo, notificationType, notificationUserIdSet))
                .toDomain();
    }

    @Override
    public NotificationInfo postLikeNotification(LikeInfo likeInfo, NotificationType notificationType, Long notificationUserId) {
        return notificationJpaRepository.save(Notification.fromLikeInfo(likeInfo, notificationType, notificationUserId))
                .toDomain();
    }
}
