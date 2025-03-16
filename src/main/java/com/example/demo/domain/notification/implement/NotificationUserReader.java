package com.example.demo.domain.notification.implement;

import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.repository.NotificationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationUserReader {
    private final NotificationUserRepository notificationUserRepository;

    public Slice<NotificationInfo> getSlice(Long userId, Long lastNotificationId, Pageable pageable) {
        return notificationUserRepository.getSlice(userId, lastNotificationId, pageable);
    }

    public Long countUnreadNotifications(Long userId) {
        return notificationUserRepository.countUnreadNotifications(userId);
    }
}
