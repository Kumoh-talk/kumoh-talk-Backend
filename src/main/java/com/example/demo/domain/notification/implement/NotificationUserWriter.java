package com.example.demo.domain.notification.implement;

import com.example.demo.domain.notification.repository.NotificationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationUserWriter {
    private final NotificationUserRepository notificationUserRepository;

    public void readNotification(Long userId, Long notificationId) {
        notificationUserRepository.readNotification(userId, notificationId);
    }

    public void deleteByUserIdAndNotificationId(Long userId, Long notificationId) {
        notificationUserRepository.deleteByUserIdAndNotificationId(userId, notificationId);
    }

}
