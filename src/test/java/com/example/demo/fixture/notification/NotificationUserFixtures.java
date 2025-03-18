package com.example.demo.fixture.notification;

import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.notification.entity.Notification;
import com.example.demo.infra.notification.entity.NotificationUser;

public class NotificationUserFixtures {
    public static NotificationUser NOTIFICATION_USER(Notification notification, User user) {
        return NotificationUser.builder()
                .notification(notification)
                .user(user)
                .isRead(false)
                .build();
    }
}
