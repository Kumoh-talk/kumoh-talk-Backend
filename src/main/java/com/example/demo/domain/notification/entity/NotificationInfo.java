package com.example.demo.domain.notification.entity;

import com.example.demo.domain.notification.entity.vo.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationInfo {
    private Long notificationId;
    private NotificationType invokerType;
    private Long invokerId;
    private Long boardId;
    private String senderNickname;
    private boolean isRead;
}
