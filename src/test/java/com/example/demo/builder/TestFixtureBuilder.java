package com.example.demo.builder;

import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.domain.comment.domain.entity.BoardComment;
import com.example.demo.domain.comment.domain.entity.RecruitmentBoardComment;
import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestFixtureBuilder {
    @Autowired
    private BuilderSupporter bs;

    public Board buildBoard(Board board) {
        return bs.boardRepository().save(board);
    }

    public User buildUser(User user) {
        return bs.userRepository().save(user);
    }

    public Like buildLike(Like like) {
        return bs.likeRepository().save(like);
    }

    public RecruitmentBoard buildRecruitmentBoard(RecruitmentBoard recruitmentBoard) {
        return bs.recruitmentBoardRepository().save(recruitmentBoard);
    }

    public BoardComment buildBoardComment(BoardComment boardComment) {
        return bs.boardCommentRepository().save(boardComment);
    }

    public RecruitmentBoardComment buildRecruitmentBoardComment(RecruitmentBoardComment recruitmentBoardComment) {
        return bs.recruitmentBoardCommentRepository().save(recruitmentBoardComment);
    }

    public Notification buildNotification(Notification notification) {
        return bs.notificationRepository().save(notification);
    }
}
