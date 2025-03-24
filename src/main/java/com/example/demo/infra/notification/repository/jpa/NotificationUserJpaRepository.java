package com.example.demo.infra.notification.repository.jpa;

import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.infra.notification.entity.NotificationUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationUserJpaRepository extends JpaRepository<NotificationUser, Long> {
    @Modifying
    @Query("UPDATE NotificationUser nu " +
            "SET nu.isRead = true " +
            "WHERE nu.user.id = :userId " +
            "AND nu.notification.id = :notificationId")
    void readNotification(Long userId, Long notificationId);

    @Modifying
    void deleteByUserIdAndNotificationId(Long userId, Long notificationId);


    @Query("SELECT COUNT(nu) FROM NotificationUser nu " +
            "WHERE nu.user.id = :userId " +
            "AND nu.isRead = false")
    Long countUnreadNotifications(Long userId);

    @Query("SELECT DISTINCT new com.example.demo.domain.notification.entity.NotificationInfo" +
            "(n.id, n.invokerType, n.invokerId, n.boardId, n.senderNickname, nu.isRead) " +
            "FROM NotificationUser nu " +
            "JOIN nu.notification n " +
            "WHERE nu.user.id = :userId " +
            "AND (:lastNotificationId IS NULL OR n.id < :lastNotificationId) " +
            "ORDER BY n.id DESC")
    Slice<NotificationInfo> findSlice(Long userId, Long lastNotificationId, Pageable pageable);

}
