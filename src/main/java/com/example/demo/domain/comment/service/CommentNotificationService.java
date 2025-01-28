package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.repository.CommonCommentRepository;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.entity.NotificationUser;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.notification.repository.NotificationUserRepository;
import com.example.demo.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentNotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationUserRepository notificationUserRepository;

    @Async
    @Transactional
    public void saveCommentNotification(Comment comment, NotificationType notificationType, CommonCommentRepository<?> commentRepository) {
        Long parentCommentId = null;
        if (comment.getParentComment() != null) {
            parentCommentId = comment.getParentComment().getId();
        }

        Set<User> userList = new HashSet<>();
        // 게시물 작성자
        if (!comment.getBoard().getUser().getId().equals(comment.getUser().getId())) {
            userList.add(comment.getBoard().getUser());
        }
        // 댓글 그룹
        commentRepository.findUsersByParentCommentId(parentCommentId)
                .stream()
                .filter(user -> !user.getId().equals(comment.getUser().getId()))
                .forEach(userList::add);
        Notification notification = Notification.fromCommentEntity(comment, notificationType);
        List<NotificationUser> notificationUserList = new ArrayList<>();

        for (User user : userList) {
            notificationUserList.add(NotificationUser.from(notification, user));
        }

        notificationRepository.save(notification);
        // TODO : 벌크 Insert 쿼리로 성능 최적화?
        notificationUserRepository.saveAll(notificationUserList);
    }
}
