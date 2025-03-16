package com.example.demo.infra.notification.repository.impl;

import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.repository.NotificationUserRepository;
import com.example.demo.infra.notification.repository.jpa.NotificationUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationUserRepositoryImpl implements NotificationUserRepository {

    private final NotificationUserJpaRepository notificationUserJpaRepository;

    @Override
    public Slice<NotificationInfo> getSlice(Long userId, Long lastNotificationId, Pageable pageable) {
        return notificationUserJpaRepository.findSlice(userId, lastNotificationId, pageable);
    }

    @Override
    public Long countUnreadNotifications(Long userId) {
        return notificationUserJpaRepository.countUnreadNotifications(userId);
    }

    @Override
    public void readNotification(Long userId, Long notificationId) {
        notificationUserJpaRepository.readNotification(userId, notificationId);
    }

    @Override
    public void deleteByUserIdAndNotificationId(Long userId, Long notificationId) {
        notificationUserJpaRepository.deleteByUserIdAndNotificationId(userId, notificationId);
    }
}
