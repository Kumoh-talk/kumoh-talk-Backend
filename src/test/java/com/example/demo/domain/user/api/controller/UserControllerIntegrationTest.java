package com.example.demo.domain.user.api.controller;


import com.example.demo.application.user.dto.request.CompleteRegistrationRequest;
import com.example.demo.base.IntegrationTest;
import com.example.demo.domain.token.repository.RefreshTokenRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtProperties;
import com.example.demo.global.jwt.JwtUserClaim;
import com.example.demo.global.utils.S3UrlUtil;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.demo.fixture.user.UserFixtures.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
public class UserControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    private User savedUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private S3UrlUtil s3UrlUtil;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp(){
        // 미리 유저 한 명 저장해두기
        savedUser = testFixtureBuilder.buildUser(SEMINAR_WRITER_USER());
    }

    @Nested
    @DisplayName("닉네임 중복 검사")
    class checkNicknameDuplicate{
        @Test
        @WithMockUser(username = "test", roles = "GUEST")
        void 성공_게스트_사용자는_닉네임_중복_검사에_통과한다() throws Exception {
            //  given
            // when & then
            ResultActions resultActions =  mockMvc.perform(
                            get("/api/v1/users/check-nickname")
                                    .param("nickname", "javaGood"))
                                    .andDo(print())
                                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "test", roles = "GUEST")
        void 실패_중복된_닉네임은_중복검사에_실패한다() throws Exception {
            //  given
            // when & then
            ResultActions resultActions =  mockMvc.perform(
                            get("/api/v1/users/check-nickname")
                                    .param("nickname", savedUser.getNickname()))
                                    .andDo(print())
                                    .andExpect(status().isConflict())
                                    .andExpect(jsonPath("$.code").value("AUTH_0005"))
                                    .andExpect(jsonPath("$.msg").value("이미 사용중인 닉네임 입니다."));
        }

        @Test
        void 실패_회원이_아닌_유저는_닉네임은_중복검사를_할수없다() throws Exception {
            //  given
            // when & then
            ResultActions resultActions =  mockMvc.perform(
                            get("/api/v1/users/check-nickname")
                                    .param("nickname", "name"))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.code").value("SECURITY_0002"))
                    .andExpect(jsonPath("$.msg").value("권한이 없습니다."));
        }
    }

    @Nested
    @DisplayName("회원가입을 위한 게스트 사용자의 추가 정보 입력")
    class completeRegistration {

        @Test
        void 성공_게스트_사용자는_추가정보_입력에_성공한다() throws Exception {
            // given
            User user = testFixtureBuilder.buildUser(GUEST_USER());

            // 기본 이미지 url
            String defaultUrl = s3UrlUtil.getDefaultImageUrl();

            JwtHandler jwtHandler = new JwtHandler(jwtProperties, refreshTokenRepository);

            JwtUserClaim claim = JwtUserClaim.create(user);

            String jwtToken = jwtHandler.createTokens(claim).getAccessToken();

            // 추가 정보 닉네임과 이름 입력
            CompleteRegistrationRequest completeRegistrationRequest = new CompleteRegistrationRequest("test", "bin");
            String jsonRequest = objectMapper.writeValueAsString(completeRegistrationRequest);

            // when & then
            ResultActions resultActions = mockMvc.perform(
                            patch("/api/v1/users/complete-registration")
                                    .header("Authorization", "Bearer " + jwtToken)
                                    .content(jsonRequest)
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print())
                                    .andExpect(status().isOk());

            User updatedUser = userJpaRepository.findById(user.getId()).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

            assertSoftly(softly -> {
                softly.assertThat(updatedUser.getProfileImageUrl()).isEqualTo(defaultUrl);
                softly.assertThat(updatedUser.getNickname()).isEqualTo(user.getNickname());
                softly.assertThat(updatedUser.getName()).isEqualTo(user.getName());
            });
        }


        @Test
        void 실패_게스트_사용자는_닉네임중복으로_추가정보_입력에_실패한다() throws Exception {
            // given
            User user = testFixtureBuilder.buildUser(GUEST_USER());

            JwtHandler jwtHandler = new JwtHandler(jwtProperties, refreshTokenRepository);

            JwtUserClaim claim = JwtUserClaim.create(user);

            String jwtToken = jwtHandler.createTokens(claim).getAccessToken();

            // 추가 정보 닉네임과 이름 입력
            CompleteRegistrationRequest completeRegistrationRequest = new CompleteRegistrationRequest(savedUser.getNickname(), "bin");
            String jsonRequest = objectMapper.writeValueAsString(completeRegistrationRequest);

            // when & then
            ResultActions resultActions = mockMvc.perform(
                            patch("/api/v1/users/complete-registration")
                                    .header("Authorization", "Bearer " + jwtToken)
                                    .content(jsonRequest)
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andDo(print())
                                    .andExpect(status().isConflict())
                                    .andExpect(jsonPath("$.code").value("AUTH_0005"))
                                    .andExpect(jsonPath("$.msg").value("이미 사용중인 닉네임 입니다."));
        }

    }

    @Nested
    @DisplayName("로그아웃")
    @Disabled
    class logout{

        @Test
        void 성공_사용자는_로그아웃에_성공한다() throws Exception {
            // given
            User user = testFixtureBuilder.buildUser(ACTIVE_USER());

            JwtHandler jwtHandler = new JwtHandler(jwtProperties, refreshTokenRepository);

            JwtUserClaim claim = JwtUserClaim.create(user);

            String jwtToken = jwtHandler.createTokens(claim).getAccessToken();

            // when & then
            mockMvc.perform(
                    patch("/api/v1/auth/logout")
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("userId",user.getId().toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());

//            assertThat(refreshTokenCrudRepository.findById(savedUser.getId())).isNotPresent();
        }
    }

    @Nested
    @DisplayName("사용자 프로필 조회")
    class getUserProfile {

        @Test
        void 성공_다른_사용자의_정보를_확인한다() throws Exception {
            // given
            Long id = savedUser.getId();

            // when & then
            mockMvc.perform(get("/api/v1/users/{id}", id))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value(savedUser.getName()))
                    .andExpect(jsonPath("$.data.nickname").value(savedUser.getNickname()))
                    .andExpect(jsonPath("$.data.profileImageUrl").value(savedUser.getProfileImageUrl()))
                    .andExpect(jsonPath("$.data.userAdditionalProfile").value(savedUser.getUserAdditionalInfo()));
        }

        @Test
        void 실패_존재하지않는_사용자의_정보는_확인할수없다() throws Exception {
            // given
            // 존재하지 않는 유저의 아이디
            Long id = 100L;

            // when & then
            mockMvc.perform(get("/api/v1/users/{id}", id))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("USER_0001"))
                    .andExpect(jsonPath("$.msg").value("해당 사용자는 존재하지 않는 사용자입니다."));
        }
    }
}
