package com.example.demo.global.oauth.user.google;

import java.util.Map;

import com.example.demo.global.oauth.user.AbstractOAuth2UserInfo;
import com.example.demo.global.oauth.user.OAuth2Provider;

public class GoogleOAuth2UserInfo extends AbstractOAuth2UserInfo {
	public GoogleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.AbstractOAuth2UserInfo(accessToken, attributes);
	}
	@Override
	protected void AbstractOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.accessToken = accessToken;
		this.attributes = attributes;
		this.id = (String) attributes.get("sub");
		this.email = (String) attributes.get("email");
		this.nickName = null;
		this.profileImageUrl = (String) attributes.get("picture");
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.GOOGLE;
	}
}
