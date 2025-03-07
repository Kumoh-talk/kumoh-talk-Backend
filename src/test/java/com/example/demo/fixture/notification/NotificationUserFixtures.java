package com.example.demo.fixture.notification;

import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.infra.user.entity.User;

public class NotificationUserFixtures {
    public static NotificationUser NOTIFICATION_USER(Notification notification, User user) {
        return NotificationUser.builder()
                .notification(notification)
                .user(user)
                .isRead(false)
                .build();
    }
}
