package com.example.demo.global.oauth.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.demo.global.oauth.user.OAuth2UserInfo;

public class OAuth2UserPrincipal implements OAuth2User, UserDetails {
	private final OAuth2UserInfo userInfo;

	public OAuth2UserPrincipal(OAuth2UserInfo oauth2UserInfo) {
		this.userInfo = oauth2UserInfo;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userInfo.getId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return userInfo.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return userInfo.getId();
	}

	public OAuth2UserInfo getUserInfo() {
		return userInfo;
	}
}
