package com.example.demo.global.oauth.user;

import java.util.Map;

import com.example.demo.global.oauth.exception.OAuth2AuthenticationProcessingException;
import com.example.demo.global.oauth.user.github.GithubOAuth2UserInfo;
import com.example.demo.global.oauth.user.google.GoogleOAuth2UserInfo;
import com.example.demo.global.oauth.user.kakao.KakaoOAuth2UserInfo;
import com.example.demo.global.oauth.user.naver.NaverOAuth2UserInfo;

public class OAuth2UserInfoFactory {

	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String,Object> attributes) {
		if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
			return new GoogleOAuth2UserInfo(accessToken, attributes);
		} else if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
			return new NaverOAuth2UserInfo(accessToken, attributes);
		} else if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
			return new KakaoOAuth2UserInfo(accessToken, attributes);
		} else if (OAuth2Provider.GITHUB.getRegistrationId().equals(registrationId)) {
			return new GithubOAuth2UserInfo(accessToken, attributes);
		} else {
			throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
		}
	}

}
