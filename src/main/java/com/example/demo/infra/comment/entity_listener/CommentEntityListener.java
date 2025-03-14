//package com.example.demo.infra.comment.entity_listener;
//
//import com.example.demo.domain.comment.domain.entity.BoardComment;
//import com.example.demo.domain.comment.domain.entity.RecruitmentBoardComment;
//import com.example.demo.domain.notification.domain.vo.NotificationType;
//import com.example.demo.infra.comment.entity.BoardComment;
//import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
//import jakarta.persistence.PreRemove;
//import lombok.NoArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@NoArgsConstructor
//public class CommentEntityListener {
//    @PreRemove
//    public void preRemove(Object comment) {
//        if (comment instanceof BoardComment boardComment) {
//            if (boardComment.getDeletedAt() == null) {
//                commentNotificationService.deleteCommentNotification(boardComment.getId(), NotificationType.BOARD_COMMENT);
//            }
//        } else if (comment instanceof RecruitmentBoardComment recruitmentBoardComment) {
//            if (recruitmentBoardComment.getDeletedAt() == null) {
//                commentNotificationService.deleteCommentNotification(recruitmentBoardComment.getId(), NotificationType.RECRUITMENT_BOARD_COMMENT);
//            }
//        }
//    }
//    // TODO : 서비스 계층 호출하지 않고, JpaRepository 빈 주입받은 후, inner class에서 비동기 호출 해야할 듯
//}
