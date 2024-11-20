package com.example.demo.global.oauth.user;

import java.util.Map;

public abstract class AbstractOAuth2UserInfo implements OAuth2UserInfo{
	protected Map<String, Object> attributes;
	protected String accessToken;
	protected String id;
	protected String email;
	protected String nickName;
	protected String profileImageUrl;

	protected abstract void AbstractOAuth2UserInfo(String accessToken, Map<String, Object> attributes);

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getNickname() {
		return nickName;
	}

	@Override
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
}
