package com.example.demo.domain.user.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteRegistration {
    private final String nickname;
    private final String name;

    public CompleteRegistration(String nickname, String name) {
        this.nickname = nickname;
        this.name = name;
    }
}
