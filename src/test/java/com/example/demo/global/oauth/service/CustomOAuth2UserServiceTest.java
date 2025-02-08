package com.example.demo.global.oauth.service;

import com.example.demo.global.config.web.RestTemplateConfig;
import com.example.demo.global.oauth.user.OAuth2Provider;
import com.example.demo.global.oauth.user.OAuth2UserInfo;
import com.example.demo.global.oauth.user.github.GithubOAuth2UserInfo;
import com.example.demo.global.oauth.user.google.GoogleOAuth2UserInfo;
import com.example.demo.global.oauth.user.kakao.KakaoOAuth2UserInfo;
import com.example.demo.global.oauth.user.naver.NaverOAuth2UserInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@ActiveProfiles("test")
@RestClientTest(value = { CustomOAuth2UserService.class })
@Import(RestTemplateConfig.class)
public class CustomOAuth2UserServiceTest {

    @Autowired
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // 외부로 HTTP 요청 후 응답을 가져오는 것이 아닌 모의 서버의 응답을 가져오도록 테스트용 RestTemplate 빈으로 교체
        ReflectionTestUtils.setField(oAuth2UserService, "restOperations", restTemplate);
    }

    @AfterEach
    void tearDown() {
        mockServer.reset();
    }

    @Nested
    @DisplayName("GitHub 사용자 정보 획득")
    class GetGithubLoginUserInformation {
        String endpointUri = "https://api.github.com/user";

        // 모의 OAuth 서버의 클라이언트 정보 및 발급하는 액세스 토큰 설정
        ClientRegistration clientRegistration = createGithubClientRegistration();
        OAuth2AccessToken accessToken = createAccessToken();
        // 모의 OAuth 서버에 보낼 요청 설정
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        @Test
        void 성공_사용자_정보를_얻을_수_있다() {
            // given
            // 모의 응답 설정
            String mockResponse = "{"
                    + "\"login\": \"octocat\","
                    + "\"id\": 123456789,"
                    + "\"node_id\": \"MDQ6VXNlcjE=\",\n"
                    + "\"avatar_url\": \"https://github.com/images/error/octocat_happy.gif\"," +
                    "\"gravatar_id\": \"\","
                    + "\"url\": \"https://api.github.com/users/octocat\","
                    + "\"html_url\": \"https://github.com/octocat\"," +
                    "\"followers_url\": \"https://api.github.com/users/octocat/followers\","
                    + "\"following_url\": \"https://api.github.com/users/octocat/following{/other_user}\","
                    + "\"gists_url\": \"https://api.github.com/users/octocat/gists{/gist_id}\","
                    + "\"starred_url\": \"https://api.github.com/users/octocat/starred{/owner}{/repo}\","
                    + "\"subscriptions_url\": \"https://api.github.com/users/octocat/subscriptions\","
                    + "\"organizations_url\": \"https://api.github.com/users/octocat/orgs\","
                    + "\"repos_url\": \"https://api.github.com/users/octocat/repos\","
                    + "\"events_url\": \"https://api.github.com/users/octocat/events{/privacy}\","
                    + "\"received_events_url\": \"https://api.github.com/users/octocat/received_events\","
                    + "\"type\": \"User\","
                    + "\"site_admin\": false,"
                    + "\"name\": \"monalisa octocat\","
                    + "\"company\": \"GitHub\","
                    + "\"blog\": \"https://github.com/blog\","
                    + "\"location\": \"San Francisco\","
                    + "\"email\": \"octocat@github.com\","
                    + "\"hireable\": false,"
                    + "\"bio\": \"There once was...\","
                    + "\"twitter_username\": \"monatheoctocat\","
                    + "\"public_repos\": 2,"
                    + "\"public_gists\": 1,"
                    + "\"followers\": 20,"
                    + "\"following\": 0,"
                    + "\"created_at\": \"2008-01-14T04:33:35Z\","
                    + "\"updated_at\": \"2008-01-14T04:33:35Z\""
                    + "}";

            // 모의 OAuth 서버 설정
            mockServer
                    // 요청의 HTTP 메서드 및 URL 지정
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    // 해당 HTTP 메서드로 해당 URL에 요청이 오면 HTTP 상태 코드 200과 함께 모의 응답을 반환
                    .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

            // when
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
            OAuth2UserPrincipal principal = (OAuth2UserPrincipal) oAuth2User;
            GithubOAuth2UserInfo userInfo = (GithubOAuth2UserInfo) principal.getUserInfo();

            // then
            assertSoftly(softly -> {
                softly.assertThat(oAuth2User).isInstanceOf(OAuth2UserPrincipal.class);
                softly.assertThat(userInfo).isInstanceOf(OAuth2UserInfo.class);

                softly.assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GITHUB);
                softly.assertThat(userInfo.getId()).isEqualTo("123456789");

                softly.assertThat(userInfo.getAccessToken()).isEqualTo(createAccessToken().getTokenValue());
            });

            mockServer.verify();
        }

        @ParameterizedTest
        @EnumSource(value = HttpStatus.class, names = { "UNAUTHORIZED", "FORBIDDEN" })
        void 실패_서버_오류가_반환돼_사용자_정보를_얻을_수_없다(HttpStatus httpStatus) {
            // given
            DefaultResponseCreator respond = withStatus(httpStatus);

            // 모의 OAuth 서버 설정
            mockServer
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(respond);

            // when & then
            assertThatThrownBy(() -> oAuth2UserService.loadUser(userRequest))
                    .isInstanceOf(AuthenticationException.class);
        }

        private ClientRegistration createGithubClientRegistration() {
            return CommonOAuth2Provider.GITHUB.getBuilder(OAuth2Provider.GITHUB.getRegistrationId())
                    .clientId("dummy-github-client-id")
                    .clientSecret("dummy-github-client-secret")
                    .build();
        }

        private OAuth2AccessToken createAccessToken() {
            return new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    "dummy-github-access-token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );
        }
    }

    @Nested
    @DisplayName("Google 사용자 정보 획득")
    class GetGoogleLoginUserInformation {
        String endpointUri = "https://www.googleapis.com/oauth2/v3/userinfo";

        // 모의 OAuth 서버의 클라이언트 정보 및 발급하는 액세스 토큰 설정
        ClientRegistration clientRegistration = createGoogleClientRegistration();
        OAuth2AccessToken accessToken = createAccessToken();
        // 모의 OAuth 서버에 보낼 요청 설정
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        @Test
        void 성공_사용자_정보를_얻을_수_있다() {
            // given
            // 모의 응답 설정
            String mockResponse = "{"
                    + "\"sub\": \"123456789012345678901\","
                    + "\"name\": \"Kim Kumoh\","
                    + "\"given_name\": \"Kumoh\","
                    + "\"family_name\": \"Kim\","
                    + "\"picture\": \"https://lh3.googleusercontent.com/a/profile_image\","
                    + "\"email\": \"example@gmail.com\","
                    + "\"email_verified\": true"
                    + "}";

            // 모의 OAuth 서버 설정
            mockServer
                    // 요청의 HTTP 메서드 및 URL 지정
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    // 해당 HTTP 메서드로 해당 URL에 요청이 오면 HTTP 상태 코드 200과 함께 모의 응답을 반환
                    .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

            // when
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
            OAuth2UserPrincipal principal = (OAuth2UserPrincipal) oAuth2User;
            GoogleOAuth2UserInfo userInfo = (GoogleOAuth2UserInfo) principal.getUserInfo();

            // then
            assertSoftly(softly -> {
                softly.assertThat(oAuth2User).isInstanceOf(OAuth2UserPrincipal.class);
                softly.assertThat(userInfo).isInstanceOf(OAuth2UserInfo.class);

                softly.assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
                softly.assertThat(userInfo.getId()).isEqualTo("123456789012345678901");

                softly.assertThat(userInfo.getAccessToken()).isEqualTo(createAccessToken().getTokenValue());
            });

            mockServer.verify();
        }

        @ParameterizedTest
        @EnumSource(value = HttpStatus.class, names = { "BAD_REQUEST", "UNAUTHORIZED", "NOT_FOUND", "INTERNAL_SERVER_ERROR" })
        void 실패_서버_오류가_반환돼_사용자_정보를_얻을_수_없다(HttpStatus httpStatus) {
            // given
            DefaultResponseCreator respond = withStatus(httpStatus);

            // 모의 OAuth 서버 설정
            mockServer
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(respond);

            // when & then
            assertThatThrownBy(() -> oAuth2UserService.loadUser(userRequest))
                    .isInstanceOf(AuthenticationException.class);
        }

        private ClientRegistration createGoogleClientRegistration() {
            return CommonOAuth2Provider.GOOGLE.getBuilder(OAuth2Provider.GOOGLE.getRegistrationId())
                    .scope("profile", "email")
                    .clientId("dummy-google-client-id")
                    .clientSecret("dummy-google-client-secret")
                    .build();
        }

        private OAuth2AccessToken createAccessToken() {
            return new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    "dummy-google-access-token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );
        }
    }

    @Nested
    @DisplayName("kakao 사용자 정보 획득")
    class GetKakaoLoginUserInformation {
        String endpointUri = "https://kapi.kakao.com/v2/user/me";

        // 모의 OAuth 서버의 클라이언트 정보 및 발급하는 액세스 토큰 설정
        ClientRegistration clientRegistration = createKakaoClientRegistration();
        OAuth2AccessToken accessToken = createAccessToken();
        // 모의 OAuth 서버에 보낼 요청 설정
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        @Test
        void 성공_사용자_정보를_얻을_수_있다() {
            // given
            // 모의 응답 설정
            String mockResponse = "{"
                    + "\"id\": 9876543210,"
                    + "\"connected_at\": \"2022-04-11T01:45:28Z\","
                    + "\"kakao_account\": {"
                    + "     \"profile_nickname_needs_agreement\": false,"
                    + "     \"profile_image_needs_agreement\": false,"
                    + "     \"profile\": {"
                    + "         \"nickname\": \"테크모\","
                    + "         \"thumbnail_image_url\": \"http://k.kakaocdn.net/dn/thumbnail.jpg\","
                    + "         \"profile_image_url\": \"http://k.kakaocdn.net/dn/profile.jpg\","
                    + "         \"is_default_image\": false,"
                    + "         \"is_default_nickname\": false"
                    + "     },"
                    + "     \"name_needs_agreement\": false,"
                    + "     \"name\": \"김금오\","
                    + "     \"email_needs_agreement\": false,"
                    + "     \"is_email_valid\": true,"
                    + "     \"is_email_verified\": true,"
                    + "     \"email\": \"test@example.com\""
                    + "}"
                    + "}";

            // 모의 OAuth 서버 설정
            mockServer
                    // 요청의 HTTP 메서드 및 URL 지정
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    // 해당 HTTP 메서드로 해당 URL에 요청이 오면 HTTP 상태 코드 200과 함께 모의 응답을 반환
                    .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

            // when
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
            OAuth2UserPrincipal principal = (OAuth2UserPrincipal) oAuth2User;
            KakaoOAuth2UserInfo userInfo = (KakaoOAuth2UserInfo) principal.getUserInfo();

            // then
            assertSoftly(softly -> {
                softly.assertThat(oAuth2User).isInstanceOf(OAuth2UserPrincipal.class);
                softly.assertThat(userInfo).isInstanceOf(OAuth2UserInfo.class);

                softly.assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.KAKAO);
                softly.assertThat(userInfo.getId()).isEqualTo("9876543210");

                softly.assertThat(userInfo.getAccessToken()).isEqualTo(createAccessToken().getTokenValue());
            });

            mockServer.verify();
        }

        @ParameterizedTest
        @EnumSource(value = HttpStatus.class, names = { "BAD_REQUEST", "UNAUTHORIZED" })
        void 실패_서버_오류가_반환돼_사용자_정보를_얻을_수_없다(HttpStatus httpStatus) {
            // given
            DefaultResponseCreator respond = withStatus(httpStatus);

            // 모의 OAuth 서버 설정
            mockServer
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(respond);

            // when & then
            assertThatThrownBy(() -> oAuth2UserService.loadUser(userRequest))
                    .isInstanceOf(AuthenticationException.class);
        }

        private ClientRegistration createKakaoClientRegistration() {
            return ClientRegistration.withRegistrationId("kakao")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                    .scope("profile_nickname", "profile_image")
                    .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                    .tokenUri("https://kauth.kakao.com/oauth/token")
                    .userInfoUri("https://kapi.kakao.com/v2/user/me")
                    .userNameAttributeName("id")
                    .clientName("Kakao")
                    .clientId("dummy-kakao-client-id")
                    .clientSecret("dummy-kakao-client-secret")
                    .build();
        }

        private OAuth2AccessToken createAccessToken() {
            return new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    "dummy-kakao-access-token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );
        }
    }

    @Nested
    @DisplayName("NAVER 사용자 정보 획득")
    class GetNaverLoginUserInformation {
        String endpointUri = "https://openapi.naver.com/v1/nid/me";

        // 모의 OAuth 서버의 클라이언트 정보 및 발급하는 액세스 토큰 설정
        ClientRegistration clientRegistration = createNaverClientRegistration();
        OAuth2AccessToken accessToken = createAccessToken();
        // 모의 OAuth 서버에 보낼 요청 설정
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        @Test
        void 성공_사용자_정보를_얻을_수_있다() {
            // given
            // 모의 응답 설정
            String mockResponse = "{"
                    + "  \"resultcode\": \"00\","
                    + "  \"message\": \"success\","
                    + "  \"response\": {"
                    + "    \"email\": \"test@example.com\","
                    + "    \"nickname\": \"테크모\","
                    + "    \"profile_image\": \"https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif\","
                    + "    \"age\": \"40-49\","
                    + "    \"gender\": \"F\","
                    + "    \"id\": \"12345678\","
                    + "    \"name\": \"김금오\","
                    + "    \"birthday\": \"10-01\","
                    + "    \"birthyear\": \"1900\","
                    + "    \"mobile\": \"010-0000-0000\""
                    + "  }"
                    + "}";

            // 모의 OAuth 서버 설정
            mockServer
                    // 요청의 HTTP 메서드 및 URL 지정
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    // 해당 HTTP 메서드로 해당 URL에 요청이 오면 HTTP 상태 코드 200과 함께 모의 응답을 반환
                    .andRespond(withSuccess(mockResponse, MediaType.APPLICATION_JSON));

            // when
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
            OAuth2UserPrincipal principal = (OAuth2UserPrincipal) oAuth2User;
            NaverOAuth2UserInfo userInfo = (NaverOAuth2UserInfo) principal.getUserInfo();

            // then
            assertSoftly(softly -> {
                softly.assertThat(oAuth2User).isInstanceOf(OAuth2UserPrincipal.class);
                softly.assertThat(userInfo).isInstanceOf(OAuth2UserInfo.class);

                softly.assertThat(userInfo.getProvider()).isEqualTo(OAuth2Provider.NAVER);
                softly.assertThat(userInfo.getId()).isEqualTo("12345678");

                softly.assertThat(userInfo.getAccessToken()).isEqualTo(createAccessToken().getTokenValue());
            });

            mockServer.verify();
        }

        @ParameterizedTest
        @EnumSource(value = HttpStatus.class, names = { "UNAUTHORIZED", "FORBIDDEN", "NOT_FOUND", "INTERNAL_SERVER_ERROR" })
        void 실패_서버_오류가_반환돼_사용자_정보를_얻을_수_없다(HttpStatus httpStatus) {
            // given
            DefaultResponseCreator respond = withStatus(httpStatus);

            // 모의 OAuth 서버 설정
            mockServer
                    .expect(requestTo(endpointUri))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(respond);

            // when & then
            assertThatThrownBy(() -> oAuth2UserService.loadUser(userRequest))
                    .isInstanceOf(AuthenticationException.class);
        }

        private ClientRegistration createNaverClientRegistration() {
            return ClientRegistration.withRegistrationId("naver")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                    .scope("name", "email", "profile_image")
                    .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                    .tokenUri("https://nid.naver.com/oauth2.0/token")
                    .userInfoUri("https://openapi.naver.com/v1/nid/me")
                    .userNameAttributeName("response")
                    .clientName("Naver")
                    .clientId("dummy-naver-client-id")
                    .clientSecret("dummy-naver-client-secret")
                    .build();
        }

        private OAuth2AccessToken createAccessToken() {
            return new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    "dummy-naver-access-token",
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );
        }
    }

}
