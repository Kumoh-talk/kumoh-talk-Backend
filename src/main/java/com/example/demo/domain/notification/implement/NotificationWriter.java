package com.example.demo.domain.notification.implement;

import com.example.demo.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationWriter {
    private final NotificationRepository notificationRepository;

}
