package com.example.demo.domain.notification.scheduler;

import com.example.demo.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final NotificationRepository notificationRepository;

    // 매일 오전 4시에 실행
    @Scheduled(cron = "0 0 4 * * ?")
    @Transactional
    public void deleteNotifications() {
        int deletedCount = notificationRepository.deleteAllZeroUsersNotifications();
        log.info("{}개의 쓰레기 알림 데이터가 삭제되었습니다.", deletedCount);
    }
}
