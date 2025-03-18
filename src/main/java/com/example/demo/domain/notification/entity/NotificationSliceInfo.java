package com.example.demo.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@AllArgsConstructor
@Builder
public class NotificationSliceInfo {
    Long unreadNotificationCount;
    Slice<NotificationInfo> notificationSlice;

    public static NotificationSliceInfo of(Long unreadNotificationCount, Slice<NotificationInfo> notificationSlice) {
        return NotificationSliceInfo.builder()
                .unreadNotificationCount(unreadNotificationCount)
                .notificationSlice(notificationSlice)
                .build();
    }
}
