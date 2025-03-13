package com.example.demo.infra.comment.entity;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.recruitment_board.domain.entity.CommentBoard;
import com.example.demo.domain.report.domain.Report;
import com.example.demo.infra.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Comment {
    Long getId();

    String getContent();

    User getUser();

    CommentBoard getBoard();

    Comment getParentComment();

    List<Comment> getReplyComments();

    LocalDateTime getCreatedAt();

    LocalDateTime getDeletedAt();

    LocalDateTime getUpdatedAt();

    void changeContent(String newContent);

    Report toReport(User user);

    CommentInfo toCommentInfoDomain();

    MyCommentInfo toMyCommentInfoDomain();
}
