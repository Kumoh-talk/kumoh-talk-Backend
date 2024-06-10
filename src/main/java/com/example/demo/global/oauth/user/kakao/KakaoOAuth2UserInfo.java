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
		// attributes 맵의 kakao_account 키의 값에 실제 attributes 맵이 할당되어 있음
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
		this.attributes = kakaoProfile;

		this.id = ((Long) attributes.get("id")).toString();
		this.email = (String) kakaoAccount.get("email");
		this.nickName = (String) attributes.get("nickname");
		this.profileImageUrl = (String) attributes.get("profile_image_url");

		this.attributes.put("id", id);
		this.attributes.put("email", this.email);
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.KAKAO;
	}
}
