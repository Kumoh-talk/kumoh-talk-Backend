package com.example.demo.infra.notification.repository.jpa;

import com.example.demo.domain.notification.entity.vo.NotificationType;
import com.example.demo.infra.notification.entity.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    @Modifying
    void deleteByInvokerIdAndInvokerType(
            @Param("invokerId") Long invokerId,
            @Param("invokerType") NotificationType notificationType);

    @Modifying
    @Query("DELETE FROM Notification n " +
            "WHERE SIZE(n.notificationUserList) = 0")
    int deleteAllZeroUsersNotifications();
}
