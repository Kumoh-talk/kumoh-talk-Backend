package com.example.demo.fixture.notification;

import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.notification.domain.vo.NotificationType;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;

public class NotificationFixtures {
    public static Notification BOARD_COMMENT_NOTIFICATION(BoardComment boardComment) {
        return Notification.builder()
                .invokerType(NotificationType.BOARD_COMMENT)
                .invokerId(boardComment.getId())
                .boardId(boardComment.getBoard().getId())
                .senderNickname(boardComment.getUser().getNickname())
                .build();
    }

    public static Notification RECRUITMENT_BOARD_COMMENT_NOTIFICATION(RecruitmentBoardComment recruitmentBoardComment) {
        return Notification.builder()
                .invokerType(NotificationType.RECRUITMENT_BOARD_COMMENT)
                .invokerId(recruitmentBoardComment.getId())
                .boardId(recruitmentBoardComment.getBoard().getId())
                .senderNickname(recruitmentBoardComment.getUser().getNickname())
                .build();
    }

    public static Notification LIKE_NOTIFICATION(Like like) {
        return Notification.builder()
                .invokerType(NotificationType.BOARD_LIKE)
                .invokerId(like.getId())
                .boardId(like.getBoard().getId())
                .senderNickname(like.getUser().getNickname())
                .build();
    }
}
