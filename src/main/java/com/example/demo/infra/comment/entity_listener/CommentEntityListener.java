package com.example.demo.infra.comment.entity_listener;

import com.example.demo.domain.notification.entity.vo.NotificationType;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.notification.repository.impl.NotificationRepositoryImpl;
import jakarta.persistence.PreRemove;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@NoArgsConstructor
public class CommentEntityListener {

    @Lazy
    @Autowired
    private NotificationRepositoryImpl notificationRepository;

    @PreRemove
    @Transactional
    public void preRemove(Object comment) {
        if (comment instanceof BoardComment boardComment) {
            if (boardComment.getDeletedAt() == null) {
                notificationRepository.deleteByInvokerIdAndInvokerType(boardComment.getId(), NotificationType.BOARD_COMMENT);
            }
        } else if (comment instanceof RecruitmentBoardComment recruitmentBoardComment) {
            if (recruitmentBoardComment.getDeletedAt() == null) {
                notificationRepository.deleteByInvokerIdAndInvokerType(recruitmentBoardComment.getId(), NotificationType.RECRUITMENT_BOARD_COMMENT);
            }
        }
    }
}
