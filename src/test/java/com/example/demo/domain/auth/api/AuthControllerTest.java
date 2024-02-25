package com.example.demo.domain.auth.api;

import com.example.demo.base.ControllerTest;
import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import com.example.demo.domain.auth.dto.response.LoginResponse;
import com.example.demo.domain.user.domain.vo.Track;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest extends ControllerTest {

    @Nested
    @DisplayName("<이메일 인증코드 보내기>")
    class emailDuplicateTest {

        String sendAuthCodeApi = "/api/auth/emails/verification-requests";

        @DisplayName("중복 이메일이 없고, 형식에 맞다면 성공")
        @ValueSource(strings = {"eee@kumoh.ac.kr", "teat@kumoh.ac.kr", "abc@kumoh.ac.kr", "test@kumoh.ac.kr"})
        @ParameterizedTest
        void successIfNoDuplicateEmail(String email) throws Exception {
            // given
            ValidateEmailRequest request = new ValidateEmailRequest(email);;

            // when -> then
            mockMvc.perform(post(sendAuthCodeApi)
                    .with(user("user").roles("USER"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andDo(document(
                            "auth/auth-duplicate-email-check-success",
                            preprocessRequest(prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").type(JsonFieldType.STRING)
                                            .description("인증 코드를 받을 이메일")
                            )
                    ));
        }

        @DisplayName("중복 이메일이 존재한다면 실패")
        @Test
        void failIfDuplicateEmailIsExist() throws Exception {
            // given
            ValidateEmailRequest request = new ValidateEmailRequest("test@kumoh.ac.kr");
            ServiceException serviceException = new ServiceException(ErrorCode.EXIST_SAME_EMAIL);

            doThrow(serviceException).when(authService).sendCodeToEmail(any(ValidateEmailRequest.class));

            // when -> then
            mockMvc.perform(post(sendAuthCodeApi)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isConflict())
                    .andDo(print())
                    .andDo(document(
                            "auth/auth-duplicate-email-check-fail-already-exist",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").type(JsonFieldType.STRING)
                                            .description("인증 코드를 받을 이메일")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @DisplayName("이메일이 유효한 형태가 아닌 경우")
        @NullAndEmptySource
        @ValueSource(strings = {"test@","test@kumoh","test@k&um","test@kumoh.123","test@naver.com"})
        @ParameterizedTest
        void failIfEmailIsNotValid(String email) throws Exception {
            // given
            ValidateEmailRequest request = new ValidateEmailRequest(email);

            // when -> then
            mockMvc.perform(post(sendAuthCodeApi)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andDo(print())
                    .andDo(document(
                            "auth/auth-duplicate-email-check-fail-not-valid-form",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("<회원가입>")
    class signupTest {

        String signupApiPath = "/api/auth/sign-up";

        JoinRequest joinRequest = new JoinRequest(
                "김건우",
                "test12345@kumoh.ac.kr",
                "123456",
                "test12345",
                Track.BACK,
                "컴퓨터공학과"
        );

        @DisplayName("요청 정보가 모두 유효하고 인증코드가 일치하면 회원가입에 성공")
        @Test
        void signupSuccess() throws Exception {
            // when -> then
            mockMvc.perform(post(signupApiPath)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(joinRequest))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andDo(document(
                            "auth/auth-signup-success",
                            preprocessRequest(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증코드"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                    fieldWithPath("track").type(JsonFieldType.STRING).description("트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING).description("전공")
                            )
                    ));
        }

        @DisplayName("빈 값이 있다면 회원가입 실패")
        @Test
        void signupFailIfFieldIsNotExist() throws Exception {
            // given
            JoinRequest invalidJoinRequest = new JoinRequest(
                    "",
                    "test12345@kumoh.ac.kr",
                    "",
                    "test12345",
                    Track.BACK,
                    ""
            );

            // when -> then
            mockMvc.perform(post(signupApiPath)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidJoinRequest))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(document(
                            "auth/auth-signup-fail-not-exist",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증코드"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                    fieldWithPath("track").type(JsonFieldType.STRING).description("트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING).description("전공")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @DisplayName("비밀번호 패턴에 부적합 시 회원가입 실패")
        @Test
        void signupFailIfPasswordNotRegex() throws Exception {
            // given
            JoinRequest invalidJoinRequest = new JoinRequest(
                    "김건우",
                    "test12345@kumoh.ac.kr",
                    "123456",
                    "test",
                    Track.BACK,
                    "컴퓨터공학과"
            );

            // when -> then
            mockMvc.perform(post(signupApiPath)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidJoinRequest))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(document(
                            "auth/auth-signup-fail-not-regex",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증코드"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                    fieldWithPath("track").type(JsonFieldType.STRING).description("트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING).description("전공")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @DisplayName("올바르지 않은 Track 입력 시 회원가입 실패")
        @Test
        void signupFailIfTrackNotFound() throws Exception {
            // given
            Map<String, String> input = new HashMap<>();
            input.put("name", "김건우");
            input.put("email", "test12345@kumoh.ac.kr");
            input.put("authCode", "123456");
            input.put("password", "test12345");
            input.put("track", "FULLSTACK");
            input.put("major", "컴퓨터공학과");


            // when -> then
            mockMvc.perform(post(signupApiPath)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andDo(document(
                            "auth/auth-signup-fail-track-not-found",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증코드"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                    fieldWithPath("track").type(JsonFieldType.STRING).description("트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING).description("전공")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @DisplayName("이미 가입된 회원이라면 가입 실패")
        @Test
        void signupFailIfAlreadyExistUSer() throws Exception {
            // given
            doThrow(new ServiceException(ErrorCode.EXIST_SAME_EMAIL)).when(authService).join(any(JoinRequest.class));

            // when -> then
            mockMvc.perform(post(signupApiPath)
                    .with(user("user").roles("USER"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(joinRequest))
            )
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ServiceException.class))
                    .andDo(document(
                            "auth/auth-signup-fail-already-exist",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증코드"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                    fieldWithPath("track").type(JsonFieldType.STRING).description("트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING).description("전공")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @DisplayName("인증코드 불일치 시 가입 실패")
        @Test
        void signupFailIfAuthCodeIsNotCorrect() throws Exception {
            // given
            doThrow(new ServiceException(ErrorCode.MISMATCH_EMAIL_AUTH_CODE)).when(authService).join(any(JoinRequest.class));

            // when -> then
            mockMvc.perform(post(signupApiPath)
                            .with(user("user").roles("USER"))
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(joinRequest))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(ServiceException.class))
                    .andDo(document(
                            "auth/auth-signup-fail-authCode-is-not-correct",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("authCode").type(JsonFieldType.STRING).description("인증코드"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                    fieldWithPath("track").type(JsonFieldType.STRING).description("트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING).description("전공")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("<로그인>")
    class loginTest {

        String loginApiPath = "/api/auth/login";

        @DisplayName("등록된 회원이라면 로그인 성공")
        @Test
        void loginSuccessIfAlreadyExistUser() throws Exception {
            // given
            LoginRequest requiredLoginRequest = new LoginRequest("test@kumoh.ac.kr", "test12345");
            LoginResponse loginResponse = new LoginResponse("MOCK_ACCESS_TOKEN", "Bearer");

            when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

            // when -> then
            mockMvc.perform(MockMvcRequestBuilders.post(loginApiPath)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requiredLoginRequest))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value(loginResponse.getAccessToken()))
                    .andExpect(jsonPath("$.tokenType").value(loginResponse.getTokenType()))
                    .andDo(print())
                    .andDo(document(
                            "auth/auth-login-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                            ),
                            responseFields(
                                    fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                            .description("AccessToken"),
                                    fieldWithPath("tokenType").type(JsonFieldType.STRING)
                                            .description("토큰 타입 (헤더)")
                            )
                    ));
        }

        @DisplayName("이메일이나 비밀번호 다를 시 로그인 실패")
        @Test
        void loginFailWrongEmailOrPassword() throws Exception {
            // given
            LoginRequest requiredLoginRequest = new LoginRequest("test@kumoh.ac.kr", "12345678");

            when(authService.login(any(LoginRequest.class))).thenThrow(new ServiceException(ErrorCode.MISMATCH_EMAIL_OR_PASSWORD));

            // when -> then
            mockMvc.perform(MockMvcRequestBuilders.post(loginApiPath)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requiredLoginRequest))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이메일 혹은 비밀번호가 틀렸습니다."))
                    .andDo(print())
                    .andDo(document(
                            "auth/auth-login-fail-wrong-email-or-password",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }
    }
}
