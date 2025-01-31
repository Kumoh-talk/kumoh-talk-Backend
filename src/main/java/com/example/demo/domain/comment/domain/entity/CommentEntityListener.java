package com.example.demo.domain.comment.domain.entity;

import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.domain.notification.repository.NotificationRepository;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CommentEntityListener {
    @Lazy
    @Autowired
    private final NotificationRepository notificationRepository;

    public CommentEntityListener() {
        notificationRepository = null;
    }

    @PreRemove
    public void preRemove(Object comment) {
        if (comment instanceof BoardComment) {
            notificationRepository.deleteByInvokerIdAndInvokerType(((BoardComment) comment).getId(), NotificationType.BOARD_COMMENT);
        } else if (comment instanceof RecruitmentBoardComment) {
            notificationRepository.deleteByInvokerIdAndInvokerType(((RecruitmentBoardComment) comment).getId(), NotificationType.RECRUITMENT_BOARD_COMMENT);
        }
    }
}
