package com.example.demo.domain.user.api.controller;

import com.example.demo.domain.user.controller.UserController;
import com.example.demo.domain.user.domain.dto.request.CompleteRegistrationRequest;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test") // application-test.yml이 로드되도록 강제
@WebMvcTest(controllers = UserController.class,
        // 보안 자동 구성을 완전히 제외하지 않으면 OAuth2 관련 빈이 로드되어 문제가 생길 수 있음.
        excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // UserController가 의존하는 UserService를 Mock 처리
    @MockBean
    private UserService userService;

    // OAuth2SecurityFilterChain 등이 필요할 경우, SecurityFilterChain을 Mock 처리해서 의존성 문제를 해결
    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Test
    @WithMockUser(username = "test", roles = {"GUEST"})
    void 성공_게스트_사용자는_닉네임_중복_검사에_통과한다() throws Exception {
        // given: 유효한 닉네임을 설정하고, userService.checkNicknameDuplicate()가 예외 없이 동작하도록 Mocking
        String nickname = "nickd";  // 정규식에 맞는 유효한 닉네임
        Mockito.doNothing().when(userService).checkNicknameDuplicate(nickname);

        // when: /api/v1/users/check-nickname에 GET 요청을 보냄
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/check-nickname")
                        .param("nickname", nickname))
                        .andDo(print());

        // then: HTTP 200 OK 응답이 반환되는지 검증
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = {"GUEST"})
    void 실패_게스트_사용자는_닉네임_중복_검사에_통과하지_못한다() throws Exception {
        // given
        String duplicatedNickname = "nickd";
        Mockito.doThrow(new ServiceException(ErrorCode.EXIST_SAME_NICKNAME)).when(userService).checkNicknameDuplicate(duplicatedNickname);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/users/check-nickname")
                        .param("nickname", duplicatedNickname))
                        .andDo(print());

        // then
        resultActions.andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "test", roles = {"GUEST"})
    void 성공_게스트_사용자는_회원가입에_필요한_추가정보를_입력한다(){
        // given
        Long id = 1L;
        CompleteRegistrationRequest completeRegistrationRequest =
                new CompleteRegistrationRequest("iLoveC","tom");
        Mockito.doNothing().when(userService).completeRegistration(id, completeRegistrationRequest);

        // when

        // then



    }

}
