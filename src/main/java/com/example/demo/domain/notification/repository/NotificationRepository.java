package com.example.demo.domain.notification.repository;

import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Modifying
    void deleteByInvokerIdAndInvokerType(Long invokerId, NotificationType notificationType);

    @Query("SELECT n FROM Notification n LEFT JOIN FETCH n.notificationUserList")
    List<Notification> findAllWithNotificationUsers();
}
