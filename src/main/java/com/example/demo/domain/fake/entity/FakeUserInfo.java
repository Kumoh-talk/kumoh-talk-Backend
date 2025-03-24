package com.example.demo.domain.fake.entity;

import com.example.demo.domain.user.vo.Role;
import com.example.demo.global.oauth.user.OAuth2Provider;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FakeUserInfo {
    private final OAuth2Provider fakeProvider;
    private final String fakeProviderId;
    private final Role fakeUserRole;
    private final String fakeUserNickname;

    @Builder
    public FakeUserInfo(OAuth2Provider fakeProvider, String fakeProviderId, Role fakeUserRole, String fakeUserNickname) {
        this.fakeProvider = fakeProvider;
        this.fakeProviderId = fakeProviderId;
        this.fakeUserRole = fakeUserRole;
        this.fakeUserNickname = fakeUserNickname;
    }
}
