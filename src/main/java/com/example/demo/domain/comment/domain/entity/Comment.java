package com.example.demo.domain.comment.domain.entity;

import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.report.domain.Report;
import com.example.demo.domain.user.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public interface Comment {
    Long getId();

    String getContent();

    User getUser();

    GenericBoard getBoard();

    Comment getParentComment();

    List<Comment> getReplyComments();

    LocalDateTime getCreatedAt();

    LocalDateTime getDeletedAt();

    LocalDateTime getUpdatedAt();

    void changeContent(String newContent);

    Report toReport(User user);
}
