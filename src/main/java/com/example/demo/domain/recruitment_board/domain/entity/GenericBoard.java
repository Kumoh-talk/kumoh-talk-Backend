package com.example.demo.domain.recruitment_board.domain.entity;

import com.example.demo.infra.user.entity.User;

public interface GenericBoard {
    Long getId();

    User getUser();
}
