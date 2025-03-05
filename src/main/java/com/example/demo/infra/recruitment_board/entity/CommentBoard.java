package com.example.demo.infra.recruitment_board.entity;

import com.example.demo.domain.user.domain.User;

import java.time.LocalDateTime;

public interface CommentBoard {
    Long getId();

    User getUser();

    String getTitle();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
