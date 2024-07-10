package com.example.demo.global.oauth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.global.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.global.oauth.user.OAuth2UserUnlinkManager;
import com.example.demo.global.oauth.service.OAuth2UserPrincipal;
import com.example.demo.global.utils.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	private static final String MODE_PARAM_COOKIE_NAME = "mode";
	private static final String LOGIN_MODE = "login";
	private static final String UNLINK_MODE = "unlink";
	private static final String LOGIN_FAILED_ERROR_MESSAGE = "Login failed";

	private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
	private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		String targetUrl = this.determineTargetUrl(request, response, authentication);

		if (response.isCommitted()) {
			log.info("Response has already been committed. Unable to redirect to " + targetUrl);
			return;
		}

		this.clearAuthenticationAttributes(request, response);
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}

	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String targetUrl = getTargetUrl(request);
		String mode = getMode(request);

		OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);
		if (principal == null) {
			return handleError(targetUrl, LOGIN_FAILED_ERROR_MESSAGE);
		}

		return switch (mode) {
			case LOGIN_MODE -> handleLogin(principal, targetUrl);
			case UNLINK_MODE -> handleUnlink(principal, targetUrl);
			default -> handleError(targetUrl, LOGIN_FAILED_ERROR_MESSAGE);
		};
	}

	private String getTargetUrl(HttpServletRequest request) {
		return CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse(getDefaultTargetUrl());
	}

	private String getMode(HttpServletRequest request) {
		return CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
			.map(Cookie::getValue)
			.orElse("");
	}

	private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2UserPrincipal) {
			return (OAuth2UserPrincipal) principal;
		}
		return null;
	}

	private String handleLogin(OAuth2UserPrincipal principal, String targetUrl) {
		// TODO: USER 정보 DB 저장
		// TODO: 액세스 토큰 (추후 REFRESH TOKEN이 도입되면 DB 저장도)

		// log 확인용
		log.info("id={}, profileImageUrl={}, email={}, nickname={}, accessToken={}",
			principal.getUserInfo().getId(),
			principal.getUserInfo().getProfileImageUrl(),
			principal.getUserInfo().getEmail(),
			principal.getUserInfo().getNickname(),
			principal.getUserInfo().getAccessToken());

		String accessToken = "test_access_token";
		String refreshToken = "test_refresh_token";

		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("access_token", accessToken)
			.queryParam("refresh_token", refreshToken)
			.build().toUriString();
	}

	private String handleUnlink(OAuth2UserPrincipal principal, String targetUrl) {
		// TODO: DB 삭제
		// TODO: 리프레시 토큰 삭제

		String accessToken = principal.getUserInfo().getAccessToken();
		OAuth2Provider provider = principal.getUserInfo().getProvider();
		oAuth2UserUnlinkManager.unlink(provider, accessToken);

		return UriComponentsBuilder.fromUriString(targetUrl)
			.build().toUriString();
	}

	private String handleError(String targetUrl, String errorMessage) {
		return UriComponentsBuilder.fromUriString(targetUrl)
			.queryParam("error", errorMessage)
			.build().toUriString();
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
		super.clearAuthenticationAttributes(request);
		httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
	}
}
