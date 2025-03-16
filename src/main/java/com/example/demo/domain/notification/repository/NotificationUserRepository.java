package com.example.demo.domain.notification.repository;

import com.example.demo.domain.notification.entity.NotificationInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationUserRepository {
    Slice<NotificationInfo> getSlice(Long userId, Long lastNotificationId, Pageable pageable);

    Long countUnreadNotifications(Long userId);

    void readNotification(Long userId, Long notificationId);

    void deleteByUserIdAndNotificationId(Long userId, Long notificationId);
}
