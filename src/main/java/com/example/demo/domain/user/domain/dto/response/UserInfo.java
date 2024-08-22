package com.example.demo.domain.user.domain.dto.response;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.oauth.user.OAuth2Provider;

public record UserInfo(
        Long id,
        OAuth2Provider provider,
        String nickname,
        String name,
        String profileImage,
        Role role
) {
    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getProvider(),
                user.getNickname(),
                user.getName(),
                user.getProfileImage(),
                user.getRole()
        );
    }
}
