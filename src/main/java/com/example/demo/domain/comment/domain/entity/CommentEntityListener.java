package com.example.demo.domain.comment.domain.entity;

import com.example.demo.domain.comment.service.CommentNotificationService;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import jakarta.persistence.PreRemove;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CommentEntityListener {

    @Lazy
    @Autowired
    private CommentNotificationService commentNotificationService;

    @PreRemove
    public void preRemove(Object comment) {
        if (comment instanceof BoardComment boardComment) {
            if (boardComment.getDeletedAt() == null) {
                commentNotificationService.deleteCommentNotification(boardComment.getId(), NotificationType.BOARD_COMMENT);
            }
        } else if (comment instanceof RecruitmentBoardComment recruitmentBoardComment) {
            if (recruitmentBoardComment.getDeletedAt() == null) {
                commentNotificationService.deleteCommentNotification(recruitmentBoardComment.getId(), NotificationType.RECRUITMENT_BOARD_COMMENT);
            }
        }
    }
}
