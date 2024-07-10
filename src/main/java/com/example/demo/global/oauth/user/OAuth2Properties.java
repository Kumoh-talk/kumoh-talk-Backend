package com.example.demo.global.oauth.user;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration")
public class OAuth2Properties {
    private Google google;
    private Kakao kakao;
    private Naver naver;
    private Github github;

    @Getter
    @Setter
    public static class Google {
        private String clientId;
    }

    @Getter
    @Setter
    public static class Kakao {

    }

    @Getter
    @Setter
    public static class Naver {
        private String clientId;
        private String clientSecret;
    }

    @Getter
    @Setter
    public static class Github {
        private String clientId;
        private String clientSecret;
    }
}
