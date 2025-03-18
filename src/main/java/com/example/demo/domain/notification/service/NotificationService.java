package com.example.demo.domain.notification.service;

import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.NotificationSliceInfo;
import com.example.demo.domain.notification.implement.NotificationUserReader;
import com.example.demo.domain.notification.implement.NotificationUserWriter;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserReader userReader;

    private final NotificationUserReader notificationUserReader;
    private final NotificationUserWriter notificationUserWriter;

    @Transactional(readOnly = true)
    public NotificationSliceInfo findNotificationListByNoOffset(Long userId, Pageable pageable, Long lastNotificationId) {
        userReader.findUserTarget(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        Slice<NotificationInfo> notificationSlice = notificationUserReader.getSlice(userId, lastNotificationId, pageable);
        Long count = notificationUserReader.countUnreadNotifications(userId);

        return NotificationSliceInfo.of(count, notificationSlice);
    }

    @Transactional
    public void readNotification(Long userId, Long notificationId) {
        userReader.findUserTarget(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        notificationUserWriter.readNotification(userId, notificationId);
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        userReader.findUserTarget(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        notificationUserWriter.deleteByUserIdAndNotificationId(userId, notificationId);
    }
}
