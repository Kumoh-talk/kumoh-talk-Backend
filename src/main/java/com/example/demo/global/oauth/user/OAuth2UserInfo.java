package com.example.demo.global.oauth.user;

import java.util.Map;

public interface OAuth2UserInfo {

	OAuth2Provider getProvider();

	String getAccessToken();

	Map<String, Object> getAttributes();

	String getId();
}
