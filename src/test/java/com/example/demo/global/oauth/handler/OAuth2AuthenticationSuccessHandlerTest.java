package com.example.demo.global.oauth.handler;

import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.fixture.user.UserFixtures;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.global.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.global.oauth.user.OAuth2UserInfo;
import com.example.demo.global.oauth.service.OAuth2UserPrincipal;
import com.example.demo.global.oauth.user.OAuth2UserUnlinkManager;
import jakarta.servlet.http.Cookie;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    private static final String REDIRECT_URI = "http://example.com/";
    private static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect-uri";
    private static final String MODE_PARAM_COOKIE_NAME = "mode";
    private static final String LOGIN_MODE = "login";
    private static final String UNLINK_MODE = "unlink";
    private static final String LOGIN_FAILED_ERROR_MESSAGE = "Login failed";

    private static final String GENERAL_PROVIDER_ID = "123456789";
    private static final OAuth2Provider NAVER_OAUTH_PROVIDER = OAuth2Provider.NAVER;
    private static final String GENERAL_PROVIDER_ACCESS_TOKEN = "dummy-google-access-token";
    private static final String DUMMY_ACCESS_TOKEN = "dummy-access-token";
    private static final String DUMMY_REFRESH_TOKEN = "dummy-refresh-token";

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository cookieRepository;

    @Mock
    private OAuth2UserUnlinkManager unlinkManager;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private JwtHandler jwtHandler;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Test
    void 성공_회원가입_후_액세스_토큰과_리프레시_토큰을_반환한다() throws Exception {
        // given
        MockHttpServletRequest request = createRequest(REDIRECT_URI, LOGIN_MODE);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // OAuth 로그인 후 데이터베이스에 해당 회원을 찾을 수 없도록 설정
        OAuth2UserPrincipal principal = createMockPrincipal(GENERAL_PROVIDER_ID, NAVER_OAUTH_PROVIDER, GENERAL_PROVIDER_ACCESS_TOKEN);
        Authentication authentication = createAuthentication(principal);
        when(userJpaRepository.findByProviderAndProviderId(NAVER_OAUTH_PROVIDER, GENERAL_PROVIDER_ID)).thenReturn(Optional.empty());

        // userRepository.save() 메서드 실행 시 지정한 회원을 반환하도록 설정
        User newUser = UserFixtures.SEMINAR_WRITER_USER();
        when(userJpaRepository.save(any(User.class))).thenReturn(newUser);

        // jwtHandler.createTokens() 메서드 실행 시 지정한 토큰을 반환하도록 설정
        TokenResponse tokenResponse = new TokenResponse(DUMMY_ACCESS_TOKEN, DUMMY_REFRESH_TOKEN);
        when(jwtHandler.createTokens(any(JwtUserClaim.class))).thenReturn(tokenResponse);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        String redirectUrl = response.getRedirectedUrl();
        assertSoftly(softly -> {
            softly.assertThat(redirectUrl).contains("is-new-user=" + true);
            softly.assertThat(redirectUrl).contains("access-token=" + DUMMY_ACCESS_TOKEN);
            softly.assertThat(redirectUrl).contains("refresh-token=" + DUMMY_REFRESH_TOKEN);
        });
    }

    @Test
    void 성공_로그인_후_액세스_토큰과_리프레시_토큰을_반환한다() throws Exception {
        // given
        MockHttpServletRequest request = createRequest(REDIRECT_URI, LOGIN_MODE);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // OAuth 로그인 후 데이터베이스에 해당 회원을 찾을 수 있도록 설정
        OAuth2UserPrincipal principal = createMockPrincipal(GENERAL_PROVIDER_ID, NAVER_OAUTH_PROVIDER, GENERAL_PROVIDER_ACCESS_TOKEN);
        Authentication authentication = createAuthentication(principal);
        User existingUser = UserFixtures.SEMINAR_WRITER_USER();
        when(userJpaRepository.findByProviderAndProviderId(NAVER_OAUTH_PROVIDER, GENERAL_PROVIDER_ID)).thenReturn(Optional.ofNullable(existingUser));

        // jwtHandler.createTokens() 메서드 실행 시 지정한 토큰을 반환하도록 설정
        TokenResponse tokenResponse = new TokenResponse(DUMMY_ACCESS_TOKEN, DUMMY_REFRESH_TOKEN);
        when(jwtHandler.createTokens(any(JwtUserClaim.class))).thenReturn(tokenResponse);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        String redirectUrl = response.getRedirectedUrl();
        assertSoftly(softly -> {
            softly.assertThat(redirectUrl).contains("is-new-user=" + false);
            softly.assertThat(redirectUrl).contains("access-token=" + DUMMY_ACCESS_TOKEN);
            softly.assertThat(redirectUrl).contains("refresh-token=" + DUMMY_REFRESH_TOKEN);
        });
    }

    @Test
    void 실패_Principal_불러오기에_실패하여_로그인할_수_없다() throws Exception {
        // given
        MockHttpServletRequest request = createRequest(REDIRECT_URI, LOGIN_MODE);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        String redirectUrl = response.getRedirectedUrl();
        assertThat(redirectUrl).contains("error=" + LOGIN_FAILED_ERROR_MESSAGE);
    }

    @Test
    void 실패_mode_쿠키가_존재하지_않아_분기할_수_없다() throws Exception {
        // given
        MockHttpServletRequest request = createRequest(REDIRECT_URI, null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        OAuth2UserPrincipal principal = createMockPrincipal(GENERAL_PROVIDER_ID, NAVER_OAUTH_PROVIDER, GENERAL_PROVIDER_ACCESS_TOKEN);
        Authentication authentication = createAuthentication(principal);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        String redirectUrl = response.getRedirectedUrl();
        assertThat(redirectUrl).contains("error=" + LOGIN_FAILED_ERROR_MESSAGE);
    }

    private MockHttpServletRequest createRequest(String redirectUri, String mode) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setCookies(new Cookie(REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri));
        if (mode != null) {
            request.setCookies(new Cookie(MODE_PARAM_COOKIE_NAME, mode));
        }

        return request;
    }

    private OAuth2UserPrincipal createMockPrincipal(String id, OAuth2Provider provider, String accessToken) {
        OAuth2UserInfo userInfo = mock(OAuth2UserInfo.class);
        lenient().when(userInfo.getId()).thenReturn(id);
        lenient().when(userInfo.getProvider()).thenReturn(provider);
        lenient().when(userInfo.getAccessToken()).thenReturn(accessToken);

        OAuth2UserPrincipal principal = mock(OAuth2UserPrincipal.class);
        lenient().when(principal.getUserInfo()).thenReturn(userInfo);

        return principal;
    }

    private Authentication createAuthentication(OAuth2UserPrincipal principal) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        return authentication;
    }

}
