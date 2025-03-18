package com.example.demo.domain.user.entity;

import com.example.demo.domain.user.vo.Role;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UpdateUserInfo {
    private final String nickname;
    private final String name;
    private final String profileImageUrl;
    private final Role role;

    private UpdateUserInfo(String nickname, String name, String profileImageUrl, Role role) {
        this.nickname = nickname;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }
}
