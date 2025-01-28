package com.example.demo.domain.notification.domain.entity;

import com.example.demo.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications_users")
@NoArgsConstructor
@Getter
public class NotificationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Builder
    public NotificationUser(Long id, Notification notification, User user, boolean isRead) {
        this.id = id;
        this.notification = notification;
        this.user = user;
        this.isRead = isRead;
    }

    public static NotificationUser from(Notification notification, User user) {
        return NotificationUser.builder()
                .notification(notification)
                .user(user)
                .isRead(false)
                .build();
    }
}
