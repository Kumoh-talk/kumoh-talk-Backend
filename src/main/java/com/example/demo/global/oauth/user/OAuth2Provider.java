package com.example.demo.global.oauth.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {
	GOOGLE("google"),
	GITHUB("github"),
	NAVER("naver"),
	KAKAO("kakao");

	private final String registrationId;
}