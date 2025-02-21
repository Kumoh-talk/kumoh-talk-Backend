package com.example.demo.domain.notification.service;

import com.example.demo.domain.notification.domain.dto.response.NotificationNoOffsetResponse;
import com.example.demo.domain.notification.domain.dto.response.NotificationResponse;
import com.example.demo.domain.notification.repository.NotificationUserRepository;
import com.example.demo.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;

    private final NotificationUserRepository notificationUserRepository;

    @Transactional(readOnly = true)
    public NotificationNoOffsetResponse findNotificationListByNoOffset(Long userId, Pageable pageable, Long lastNotificationId) {
        userService.validateUser(userId);
        Slice<NotificationResponse> notificationSlice = notificationUserRepository.findSliceByNoOffset(userId, lastNotificationId, pageable);
        Long count = notificationUserRepository.countUnreadNotifications(userId);

        return NotificationNoOffsetResponse.from(notificationSlice.hasNext(), notificationSlice.getSize(), count, notificationSlice.getContent());
    }

    @Transactional
    public void readNotification(Long userId, Long notificationId) {
        userService.validateUser(userId);

        notificationUserRepository.readNotification(userId, notificationId);
    }

    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        userService.validateUser(userId);

        notificationUserRepository.deleteByUser_IdAndNotification_Id(userId, notificationId);
    }
}
