package com.example.demo.global.oauth.user.naver;

import java.util.Map;

import com.example.demo.global.oauth.user.AbstractOAuth2UserInfo;
import com.example.demo.global.oauth.user.OAuth2Provider;

public class NaverOAuth2UserInfo extends AbstractOAuth2UserInfo {

	public NaverOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.AbstractOAuth2UserInfo(accessToken, attributes);
	}

	@Override
	protected void AbstractOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.accessToken = accessToken;
		// attributes 맵의 response 키의 값에 실제 attributes 맵이 할당되어 있음
		this.attributes = (Map<String, Object>) attributes.get("response");
		this.id = (String) this.attributes.get("id");
		this.email = (String) this.attributes.get("email");
		this.nickName = (String) attributes.get("nickname");
		this.profileImageUrl = (String) attributes.get("profile_image");
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.NAVER;
	}
}
