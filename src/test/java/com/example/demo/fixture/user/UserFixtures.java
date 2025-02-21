package com.example.demo.fixture.user;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.global.oauth.user.OAuth2Provider;

public class UserFixtures {

    private static final String GENERAL_NICKNAME = "testName";
    private static final String GENERAL_NICKNAME2 = "testnAme";
    private static final String GENERAL_NICKNAME3 = "";
    private static final String GENERAL_NICKNAME4 = "testnaMe";
    private static final String GENERAL_NICKNAME5 = "testnamE";

    private static final String GENERAL_PROVIDER_ID = "123456789";
    private static final OAuth2Provider NAVER_OAUTH_PROVIDER = OAuth2Provider.NAVER;

    private static final Role ADMIN_ROLE = Role.ROLE_ADMIN;
    private static final Role ROLE_SEMINAR_WRITER = Role.ROLE_SEMINAR_WRITER;
    private static final Role ROLE_ACTIVE_USER = Role.ROLE_ACTIVE_USER;
    private static final Role ROLE_USER = Role.ROLE_USER;
    private static final Role ROLE_GUEST = Role.ROLE_GUEST;

    public static User ADMIN_USER() {
        return User.builder()
                .provider(NAVER_OAUTH_PROVIDER)
                .providerId(GENERAL_PROVIDER_ID)
                .nickname(GENERAL_NICKNAME)
                .role(ADMIN_ROLE)
                .build();
    }


    public static User SEMINAR_WRITER_USER() {
        return User.builder()
                .provider(NAVER_OAUTH_PROVIDER)
                .providerId(GENERAL_PROVIDER_ID)
                .nickname(GENERAL_NICKNAME2)
                .role(ROLE_SEMINAR_WRITER)
                .build();
    }

    public static User USER() {
        return User.builder()
                .provider(NAVER_OAUTH_PROVIDER)
                .providerId(GENERAL_PROVIDER_ID)
                .nickname(GENERAL_NICKNAME5)
                .role(ROLE_USER)
                .build();
    }

    public static User GUEST_USER() {
        return User.builder()
                .provider(NAVER_OAUTH_PROVIDER)
                .providerId(GENERAL_PROVIDER_ID)
                .nickname(GENERAL_NICKNAME3)
                .role(ROLE_GUEST)
                .build();
    }

    public static User ACTIVE_USER() {
        return User.builder()
                .provider(NAVER_OAUTH_PROVIDER)
                .providerId(GENERAL_PROVIDER_ID)
                .nickname(GENERAL_NICKNAME4)
                .role(ROLE_ACTIVE_USER)
                .build();
    }

}
