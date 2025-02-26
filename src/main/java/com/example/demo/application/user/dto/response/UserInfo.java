package com.example.demo.application.user.dto.response;

import com.example.demo.application.user.dto.vo.Role;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.infra.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public record UserInfo(
        Long id,
        OAuth2Provider provider,
        String nickname,
        String name,
        String profileImageUrl,
        Role role,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt
) {
    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getProvider(),
                user.getNickname(),
                user.getName(),
                user.getProfileImageUrl(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
