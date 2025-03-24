package com.example.demo.domain.notification.implement;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.implement.board.GenericCommentBoardReader;
import com.example.demo.domain.comment.implement.comment.AbstractCommentHandler;
import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommentNotificationWriter {

    private final NotificationRepository notificationRepository;

    @Async
    @Transactional
    public CompletableFuture<NotificationInfo> postCommentNotification(
            CommentInfo comment, NotificationType notificationType,
            AbstractCommentHandler<?> commentRepository, GenericCommentBoardReader commentBoardReader) {
        Long parentCommentId = null;
        if (comment.getGroupId() != null) {
            parentCommentId = comment.getGroupId();
        }

        Set<Long> notificationUserIdSet = new HashSet<>();

        // 부모 댓글 작성시 알림 대상자 : 게시물 작성자 + 다른 부모 댓글 대상자
        // 대댓글 작성 시 알림 대상자 : 게시물 작성자 + 부모 댓글 작성자 + 같은 부모를 가지는 대댓글 작성자
        Long boardWriterId = commentBoardReader.getUserIdById(comment.getBoardId());
        if (!boardWriterId.equals(comment.getCommentUserInfo().getUserId())) {
            notificationUserIdSet.add(boardWriterId);
        }
        commentRepository.getUsersByBoardIdAndParentCommentId(comment.getBoardId(), parentCommentId)
                .stream()
                .filter(user -> !user.getUserId().equals(comment.getCommentUserInfo().getUserId()))
                .forEach(user -> notificationUserIdSet.add(user.getUserId()));

        if (!notificationUserIdSet.isEmpty()) {
            return CompletableFuture.completedFuture(notificationRepository.postCommentNotification(comment, notificationType, notificationUserIdSet));
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }
}