package com.example.demo.domain.auth.api;

import com.example.demo.base.ControllerTest;
import com.example.demo.domain.auth.dto.request.JoinRequest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTest {

    @Nested
    @DisplayName("<이메일 중복검사>")
    class emailDuplicateTest {

        String duplicateApi = "/api/auth/duplicate";

        @DisplayName("중복 이메일이 없다면 성공")
        @ValueSource(strings = {"test@test.com", "teat@test.com", "test@naver.com", "test@kumoh.ac.kr"})
        @ParameterizedTest
        void successIfNoDuplicateEmail(String email) throws Exception {
            // given
            ValidateEmailRequest request = new ValidateEmailRequest(email);

            when(authService.isNotExistEmail(email)).thenReturn(true);

            // when -> then
            mockMvc.perform(post(duplicateApi)
                    .with(user("user").roles("USER"))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNoContent())
                    .andDo(print())
                    .andDo(document(
                            "auth/auth-duplicate-email-check-success",
                            preprocessRequest(prettyPrint()),
                            requestFields(
                                    fieldWithPath("email").type(JsonFieldType.STRING)
                                            .description("중복 검사 대상 이메일")
                            )
                    ));
        }

        @DisplayName("중복 이메일이 존재한다면 실패")
        @Test
        void failIfDuplicateEmailIsExist() throws Exception {
            // given
            ValidateEmailRequest request = new ValidateEmailRequest("test@kumoh.ac.kr");
            ServiceException serviceException = new ServiceException(ErrorCode.EXIST_SAME_EMAIL);

            doThrow(serviceException).when(authService).isNotExistEmail(request.getEmail());

            // when -> then
            mockMvc.perform(post(duplicateApi)
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
                                            .description("중복 검사 대상 이메일")
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
        @ValueSource(strings = {"test@","test@kumoh","test@k&um","test@kumoh.123","@kumoh.ac.kr"})
        @ParameterizedTest
        void failIfEmailIsNotValid(String email) throws Exception {
            // given
            ValidateEmailRequest request = new ValidateEmailRequest(email);

            // when -> then
            mockMvc.perform(post(duplicateApi)
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
                "test12345",
                Track.BACK,
                "컴퓨터공학과"
        );

        @DisplayName("요청 정보가 모두 유효하면 회원가입에 성공")
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

}
