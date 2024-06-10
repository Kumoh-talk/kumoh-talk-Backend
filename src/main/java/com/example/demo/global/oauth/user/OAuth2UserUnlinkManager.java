package com.example.demo.global.oauth.user;

import org.springframework.stereotype.Component;

import com.example.demo.global.oauth.exception.OAuth2AuthenticationProcessingException;
import com.example.demo.global.oauth.user.github.GithubOAuth2UserUnlink;
import com.example.demo.global.oauth.user.google.GoogleOAuth2UserUnlink;
import com.example.demo.global.oauth.user.kakao.KakaoOAuth2UserUnlink;
import com.example.demo.global.oauth.user.naver.NaverOAuth2UserUnlink;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2UserUnlinkManager {
	private final GoogleOAuth2UserUnlink googleOAuth2UserUnlink;
	private final KakaoOAuth2UserUnlink kakaoOAuth2UserUnlink;
	private final NaverOAuth2UserUnlink naverOAuth2UserUnlink;
	private final GithubOAuth2UserUnlink githubOAuth2UserUnlink;

	public void unlink(OAuth2Provider provider, String accessToken) {
		if (OAuth2Provider.GOOGLE.equals(provider)) {
			googleOAuth2UserUnlink.unlink(accessToken);
		} else if (OAuth2Provider.NAVER.equals(provider)) {
			naverOAuth2UserUnlink.unlink(accessToken);
		} else if (OAuth2Provider.KAKAO.equals(provider)) {
			kakaoOAuth2UserUnlink.unlink(accessToken);
		} else if (OAuth2Provider.GITHUB.equals(provider)) {
			githubOAuth2UserUnlink.unlink(accessToken);
		}else {
			throw new OAuth2AuthenticationProcessingException("Unlink with " + provider.getRegistrationId() + " is not supported");
		}
	}
}
