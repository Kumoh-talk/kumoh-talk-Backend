package com.example.demo.fixture.comment;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;

public class CommentFixtures {
    private static final String GENERAL_CONTENT = "content";

    public static BoardComment BOARD_COMMENT(Board board, User user, BoardComment parentComment) {
        return BoardComment.builder()
                .content(GENERAL_CONTENT)
                .board(board)
                .user(user)
                .parentComment(parentComment)
                .build();
    }

    public static RecruitmentBoardComment RECRUITMENT_BOARD_COMMENT(RecruitmentBoard recruitmentBoard, User user, RecruitmentBoardComment parentComment) {
        return RecruitmentBoardComment.builder()
                .content(GENERAL_CONTENT)
                .recruitmentBoard(recruitmentBoard)
                .user(user)
                .parentComment(parentComment)
                .build();
    }
}
