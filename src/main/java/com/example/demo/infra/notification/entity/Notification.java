package com.example.demo.infra.notification.entity;

import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.vo.NotificationType;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "invoker_type", nullable = false)
    private NotificationType invokerType;

    @Column(name = "invoker_id", nullable = false)
    private Long invokerId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "sender_nickname", nullable = false)
    private String senderNickname;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NotificationUser> notificationUserList = new ArrayList<>();

    @Builder
    public Notification(NotificationType invokerType, Long invokerId, Long boardId, String senderNickname) {
        this.invokerType = invokerType;
        this.invokerId = invokerId;
        this.boardId = boardId;
        this.senderNickname = senderNickname;
    }

    public static Notification fromCommentInfo(CommentInfo commentInfo, NotificationType notificationType, Set<Long> notificationUserIdSet) {
        Notification notification = Notification.builder()
                .invokerType(notificationType)
                .invokerId(commentInfo.getCommentId())
                .boardId(commentInfo.getBoardId())
                .senderNickname(commentInfo.getCommentUserInfo().getNickName())
                .build();

        List<NotificationUser> notificationUserList = notificationUserIdSet.stream()
                .map(userId -> new NotificationUser(notification, userId))
                .toList();
        notification.notificationUserList.addAll(notificationUserList);
        return notification;
    }

    public static Notification fromLikeInfo(LikeInfo likeInfo, NotificationType notificationType, Long notificationUserId) {
        Notification notification = Notification.builder()
                .invokerType(notificationType)
                .invokerId(likeInfo.getLikeId())
                .boardId(likeInfo.getBoardId())
                .senderNickname(likeInfo.getUserNickname())
                .build();

        NotificationUser notificationUser = new NotificationUser(notification, notificationUserId);
        notification.notificationUserList.add(notificationUser);
        return notification;
    }

    public NotificationInfo toDomain() {
        return NotificationInfo.builder()
                .notificationId(id)
                .invokerType(invokerType)
                .invokerId(invokerId)
                .boardId(boardId)
                .senderNickname(senderNickname)
                .build();
    }
}
