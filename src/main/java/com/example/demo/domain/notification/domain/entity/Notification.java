package com.example.demo.domain.notification.domain.entity;

import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    public static Notification fromCommentEntity(Comment comment, NotificationType notificationType) {
        return Notification.builder()
                .invokerType(notificationType)
                .invokerId(comment.getId())
                .boardId(comment.getBoard().getId())
                .senderNickname(comment.getUser().getNickname())
                .build();
    }

    public static Notification fromLikeEntity(Like like, NotificationType notificationType) {
        return Notification.builder()
                .invokerType(notificationType)
                .invokerId(like.getId())
                .boardId(like.getBoard().getId())
                .senderNickname(like.getUser().getNickname())
                .build();
    }
}
