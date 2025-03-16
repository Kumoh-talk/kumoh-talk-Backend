package com.example.demo.domain.notification.repository;

import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.vo.NotificationType;

import java.util.Set;

public interface NotificationRepository {
    int deleteAllZeroUsersNotifications();

    void deleteByInvokerIdAndInvokerType(Long invokerId, NotificationType invokerType);

    NotificationInfo postCommentNotification(CommentInfo comment, NotificationType notificationType, Set<Long> notificationUserIdSet);

    NotificationInfo postLikeNotification(LikeInfo comment, NotificationType notificationType, Long notificationUserId);

}
