package com.example.demo.global.oauth.user.kakao;

import java.util.Map;

import com.example.demo.global.oauth.user.AbstractOAuth2UserInfo;
import com.example.demo.global.oauth.user.OAuth2Provider;

public class KakaoOAuth2UserInfo extends AbstractOAuth2UserInfo {

	public KakaoOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.AbstractOAuth2UserInfo(accessToken, attributes);
	}

	@Override
	protected void AbstractOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.accessToken = accessToken;
		this.id = ((Long) attributes.get("id")).toString();
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.KAKAO;
	}
}
