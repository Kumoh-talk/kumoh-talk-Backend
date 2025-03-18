package com.example.demo.domain.user.entity;

import com.example.demo.domain.user.vo.Role;
import com.example.demo.global.oauth.user.OAuth2Provider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInfo {
    private final Long id;
    private final OAuth2Provider provider;
    private final String nickname;
    private final String name;
    private final String profileImageUrl;
    private final Role role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    private UserInfo(Long id, OAuth2Provider provider, String nickname, String name, String profileImageUrl, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.provider = provider;
        this.nickname = nickname;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
