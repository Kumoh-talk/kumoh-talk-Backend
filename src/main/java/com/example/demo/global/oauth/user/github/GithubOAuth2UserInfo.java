package com.example.demo.global.oauth.user.github;

import java.util.Map;

import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.global.oauth.user.AbstractOAuth2UserInfo;

public class GithubOAuth2UserInfo extends AbstractOAuth2UserInfo {

	public GithubOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.AbstractOAuth2UserInfo(accessToken, attributes);
	}

	@Override
	protected void AbstractOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
		this.accessToken = accessToken;
		this.id = String.valueOf(attributes.get("id"));
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.GITHUB;
	}
}
