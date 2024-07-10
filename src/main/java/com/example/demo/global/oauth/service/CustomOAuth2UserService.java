package com.example.demo.global.oauth.service;



import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.global.oauth.user.OAuth2UserInfo;
import com.example.demo.global.oauth.user.OAuth2UserInfoFactory;
import com.example.demo.global.oauth.exception.OAuth2AuthenticationProcessingException;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);

		try {
			return processOauth2User(userRequest, oauth2User);
		} catch (AuthenticationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}
	}

	private OAuth2User processOauth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		String accessToken = userRequest.getAccessToken().getTokenValue();
		OAuth2UserInfo oauth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId,accessToken,oauth2User.getAttributes());

		if(!StringUtils.hasText(oauth2UserInfo.getId())) {
			throw new OAuth2AuthenticationProcessingException("Id not found from OAuth2 provider");
		}

		return new OAuth2UserPrincipal(oauth2UserInfo);
	}
}
