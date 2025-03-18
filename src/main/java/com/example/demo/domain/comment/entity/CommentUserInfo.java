package com.example.demo.domain.comment.entity;

import com.example.demo.domain.user.vo.Role;
import com.example.demo.infra.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUserInfo {
    private final Long userId;
    private final String nickName;
    private final Role userRole;
    private final String profileImageUrl;

    @Builder
    public CommentUserInfo(Long userId, String nickName, Role userRole, String profileImageUrl) {
        this.userId = userId;
        this.nickName = nickName;
        this.userRole = userRole;
        this.profileImageUrl = profileImageUrl;
    }

    public static CommentUserInfo from(User user) {
        return CommentUserInfo.builder()
                .userId(user.getId())
                .nickName(user.getNickname())
                .userRole(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
