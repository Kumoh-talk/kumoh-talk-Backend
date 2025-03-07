package com.example.demo.application.user.dto.response;

import com.example.demo.domain.user.vo.Role;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public record UserInfoResponse(
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
    public static UserInfoResponse toUserInfoResponse(UserInfo userInfo) {
        return new UserInfoResponse(
                userInfo.getId(),
                userInfo.getProvider(),
                userInfo.getNickname(),
                userInfo.getName(),
                userInfo.getProfileImageUrl(),
                userInfo.getRole(),
                userInfo.getCreatedAt(),
                userInfo.getUpdatedAt()
        );
    }
}
