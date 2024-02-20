package com.example.demo.domain.auth.api;

import com.example.demo.base.ControllerTest;
import com.example.demo.domain.auth.dto.request.ValidateEmailRequest;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
                    .andExpect(status().isOk())
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

        @DisplayName("닉네임이 유효한 형태가 아닌 경우")
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
}
