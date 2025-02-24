package com.example.demo.global.oauth.handler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
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

    private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect-uri";
    private static final String MODE_PARAM_COOKIE_NAME = "mode";
    private static final String LOGIN_MODE = "login";
    private static final String UNLINK_MODE = "unlink";
    private static final String LOGIN_FAILED_ERROR_MESSAGE = "Login failed";

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHandler jwtHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl = this.determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.info("Response has already been committed. Unable to redirect to {}", targetUrl);
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
        AtomicReference<Boolean> isNewUser = new AtomicReference<>(false);
        String providerId = principal.getUserInfo().getId();
        OAuth2Provider provider = principal.getUserInfo().getProvider();

        User user = userJpaRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    isNewUser.set(true);
                    return createAndSaveNewUser(providerId, provider);
                });

        if (user.getRole().equals(Role.ROLE_GUEST)) {
            isNewUser.set(true);
        }

        TokenResponse tokens = jwtHandler.createTokens(JwtUserClaim.create(user));

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("is-new-user", isNewUser.get())
                .queryParam("access-token", tokens.accessToken())
                .queryParam("refresh-token", tokens.refreshToken())
                .build().toUriString();
    }

    private User createAndSaveNewUser(String providerId, OAuth2Provider provider) {
        User user = User
                .builder()
                .providerId(providerId)
                .provider(provider)
                .role(Role.ROLE_GUEST)
                .build();

        return userJpaRepository.save(user);
    }

    private String handleUnlink(OAuth2UserPrincipal principal, String targetUrl) {
        OAuth2Provider provider = principal.getUserInfo().getProvider();

        oAuth2UserUnlinkManager.unlink(provider, principal.getUserInfo().getAccessToken());

        userJpaRepository.findByProviderAndProviderId(provider, principal.getUserInfo().getId())
                .ifPresent(user -> {
                    userJpaRepository.delete(user);
                    refreshTokenRepository.deleteById(user.getId());
                });

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
