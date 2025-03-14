package com.example.demo.domain.recruitment_board.domain.entity;

import com.example.demo.domain.user.domain.User;

import java.time.LocalDateTime;

public interface CommentBoard {
    Long getId();

    User getUser();

    String getTitle();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
