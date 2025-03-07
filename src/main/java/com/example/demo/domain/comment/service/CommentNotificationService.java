package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.repository.CommonCommentRepository;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.infra.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommentNotificationService {

    private final NotificationRepository notificationRepository;

    @Async
    @Transactional
    public CompletableFuture<Notification> saveCommentNotification(Comment comment, NotificationType notificationType, CommonCommentRepository<?> commentRepository) {
        Long parentCommentId = null;
        if (comment.getParentComment() != null) {
            parentCommentId = comment.getParentComment().getId();
        }

        Set<User> userList = new HashSet<>();

        // 부모 댓글 작성시 알림 대상자 : 게시물 작성자 + 다른 부모 댓글 대상자
        // 대댓글 작성 시 알림 대상자 : 게시물 작성자 + 부모 댓글 작성자 + 같은 부모를 가지는 대댓글 작성자
        if (!comment.getBoard().getUser().getId().equals(comment.getUser().getId())) {
            userList.add(comment.getBoard().getUser());
        }
        commentRepository.findUsersByBoard_idByParentComment_id(comment.getBoard().getId(), parentCommentId)
                .stream()
                .filter(user -> !user.getId().equals(comment.getUser().getId()))
                .forEach(userList::add);
        Notification notification = Notification.fromCommentEntity(comment, notificationType);

        for (User user : userList) {
            notification.getNotificationUserList()
                    .add(NotificationUser.from(notification, user));
        }
        if (!notification.getNotificationUserList().isEmpty()) {
            return CompletableFuture.completedFuture(notificationRepository.save(notification));
        } else {
            return CompletableFuture.completedFuture(null);
        }

    }

    @Async
    @Transactional
    public void deleteCommentNotification(Long invokerId, NotificationType invokerType) {
        notificationRepository.deleteByInvokerIdAndInvokerType(invokerId, invokerType);
    }
}
