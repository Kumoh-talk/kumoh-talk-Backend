package com.example.demo.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

@Getter
public class LoginRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = EMAIL_REGEXP, message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
