package com.example.demo.domain.user.api;

import com.example.demo.base.ControllerTest;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Track;
import com.example.demo.domain.user.dto.request.UserPasswordUpdateRequest;
import com.example.demo.domain.user.dto.request.UserUpdateRequest;
import com.example.demo.domain.user.dto.response.UserInfoResponse;
import com.example.demo.domain.user.dto.response.UserUpdateResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {

    @Nested
    @DisplayName("<유저 프로필 조회>")
    class getUserProfile {

        String getUserProfileUrl = "/api/users/profile";

        @Test
        @DisplayName("인증된 유저인 경우 프로필 조회성공")
        void successGetUserProfile() throws Exception {
            // given
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
            User user = User.builder()
                    .id(1L)
                    .name("테스트")
                    .email("test@kumoh.ac.kr")
                    .track(Track.BACK)
                    .major("컴퓨터공학과")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            UserInfoResponse response = new UserInfoResponse(user);

            given(userService.getUserProfile(any())).willReturn(response);

            // when -> then
            mockMvc.perform(get(getUserProfileUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {ACCESS_TOKEN}")
                            .with(user("user").roles("USER"))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(response.getId()))
                    .andExpect(jsonPath("$.name").value(response.getName()))
                    .andExpect(jsonPath("$.email").value(response.getEmail()))
                    .andExpect(jsonPath("$.role").value(response.getRole().toString()))
                    .andExpect(jsonPath("$.track").value(response.getTrack().toString()))
                    .andExpect(jsonPath("$.major").value(response.getMajor()))
                    .andExpect(jsonPath("$.createdAt").value(response.getCreatedAt().format(formatter)))
                    .andExpect(jsonPath("$.updatedAt").value(response.getUpdatedAt().format(formatter)))
                    .andDo(document("user/user-get-profile-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("Access Token")
                            ),
                            responseFields(
                                    fieldWithPath("id").type(JsonFieldType.NUMBER)
                                            .description("사용자 ID"),
                                    fieldWithPath("name").type(JsonFieldType.STRING)
                                            .description("사용자 이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING)
                                            .description("사용자 이메일"),
                                    fieldWithPath("role").type(JsonFieldType.STRING)
                                            .description("사용자 권한"),
                                    fieldWithPath("track").type(JsonFieldType.STRING)
                                            .description("사용자 트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING)
                                            .description("사용자 전공 분야"),
                                    fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                            .description("만든 날짜"),
                                    fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                            .description("수정된 날짜")
                            )
                    ));
        }

        @Test
        @DisplayName("등록되지 않은 유저라면 프로필 조회 실패")
        void failIfUserNotFound() throws Exception {
            // given
            given(userService.getUserProfile(any())).willThrow(
                    new ServiceException(ErrorCode.FAIL_USER_LOGIN));

            // when -> then
            mockMvc.perform(get(getUserProfileUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {ACCESS_TOKEN}")
                            .with(user("user").roles("USER"))
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andDo(document("user/user-not-found-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("Access Token")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @Test
        @DisplayName("토큰이 없다면 프로필 조회 실패")
        void failIfTokenNotFound() throws Exception {
            // given
            given(userService.getUserProfile(any())).willThrow(
                    new ServiceException(ErrorCode.NEED_AUTHORIZED));

            // when -> then
            mockMvc.perform(get(getUserProfileUrl)
                            .with(csrf())
                            .header("Authorization", "")
                            .with(user("user").roles("USER"))
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andDo(document("user/token-not-found-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @Test
        @DisplayName("토큰이 유효하지 않다면 프로필 조회 실패")
        void failIfInvalidToken() throws Exception {
            // given
            given(userService.getUserProfile(any())).willThrow(
                    new ServiceException(ErrorCode.INVALID_TOKEN));

            // when -> then
            mockMvc.perform(get(getUserProfileUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {INVALID_TOKEN}")
                            .with(user("user").roles("USER"))
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andDo(document("user/invalid-token-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("INVALID_TOKEN")
                            ),
                            responseFields(
                                    fieldWithPath("timestamp").type(JsonFieldType.STRING)
                                            .description("응답 시간"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("응답 메시지")
                            )
                    ));
        }

        @Test
        @DisplayName("토큰이 만료되었다면 프로필 조회 실패")
        void failIfExpiredToken() throws Exception {
            // given
            given(userService.getUserProfile(any())).willThrow(
                    new ServiceException(ErrorCode.EXPIRED_TOKEN));

            // when -> then
            mockMvc.perform(get(getUserProfileUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {EXPIRED_TOKEN}")
                            .with(user("user").roles("USER"))
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andDo(document("user/expired-token-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("EXPIRED_TOKEN")
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
    @DisplayName("<유저 프로필 수정>")
    class updateUserProfile {
        String updateUserProfileUrl = "/api/users/profile";

        @Test
        @DisplayName("인증된 유저인 경우 프로필 수정 성공")
        void successGetUserProfile() throws Exception {
            // given
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
            UserUpdateRequest request = new UserUpdateRequest("수정된이름", Track.FRONT, "컴퓨터소프트웨어공학과");
            User user = User.builder()
                    .id(1L)
                    .name("수정된이름")
                    .email("test@kumoh.ac.kr")
                    .track(Track.FRONT)
                    .major("컴퓨터소프트웨어공학과")
                    .updatedAt(LocalDateTime.now())
                    .build();
            UserUpdateResponse response = UserUpdateResponse.from(user);

            given(userService.updateUserProfile(any(), any())).willReturn(response);

            // when -> then
            mockMvc.perform(patch(updateUserProfileUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {ACCESS_TOKEN}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(response.getName()))
                    .andExpect(jsonPath("$.track").value(response.getTrack().toString()))
                    .andExpect(jsonPath("$.major").value(response.getMajor()))
                    .andExpect(jsonPath("$.updatedAt").value(response.getUpdatedAt().format(formatter)))
                    .andDo(document("user/user-update-profile-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("Access Token")
                            ),
                            requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING)
                                            .description("수정할 사용자 이름"),
                                    fieldWithPath("track").type(JsonFieldType.STRING)
                                            .description("수정할 사용자 트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING)
                                            .description("수정할 사용자 전공 분야")
                            ),
                            responseFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING)
                                            .description("수정된 사용자 이름"),
                                    fieldWithPath("track").type(JsonFieldType.STRING)
                                            .description("수정된 사용자 트랙"),
                                    fieldWithPath("major").type(JsonFieldType.STRING)
                                            .description("수정된 사용자 전공 분야"),
                                    fieldWithPath("updatedAt").type(JsonFieldType.STRING)
                                            .description("수정된 날짜")
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("<유저 비밀번호 수정>")
    class updateUserPassword {
        String updateUserPasswordUrl = "/api/users/profile-password";

        @Test
        @DisplayName("기존 비밀번호가 정확하다면, 비밀번호 변경 성공")
        void successPatchUserPassword() throws Exception {
            // given
            UserPasswordUpdateRequest request = new UserPasswordUpdateRequest("test12345", "qwerty1234");


            // when -> then
            mockMvc.perform(patch(updateUserPasswordUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {ACCESS_TOKEN}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("user/user-update-password-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("Access Token")
                            ),
                            requestFields(
                                    fieldWithPath("oldPassword").type(JsonFieldType.STRING)
                                            .description("기존 비밀번호"),
                                    fieldWithPath("newPassword").type(JsonFieldType.STRING)
                                            .description("수정할 비밀번호")
                            )
                    ));
        }

        @Test
        @DisplayName("기존 비밀번호와 다르다면, 비밀번호 변경 실패")
        void failIfOldPasswordInValid() throws Exception {
            // given
            UserPasswordUpdateRequest request = new UserPasswordUpdateRequest("invalid11", "qwerty1234");

            doThrow(new ServiceException(ErrorCode.INVALID_PASSWORD)).when(userService).updateUserPassword(any(),any(UserPasswordUpdateRequest.class));

            // when -> then
            mockMvc.perform(patch(updateUserPasswordUrl)
                            .with(csrf())
                            .header("Authorization", "Bearer {ACCESS_TOKEN}")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andDo(document("user/user-update-password-fail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("Access Token")
                            ),
                            requestFields(
                                    fieldWithPath("oldPassword").type(JsonFieldType.STRING)
                                            .description("기존 비밀번호"),
                                    fieldWithPath("newPassword").type(JsonFieldType.STRING)
                                            .description("수정할 비밀번호")
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
