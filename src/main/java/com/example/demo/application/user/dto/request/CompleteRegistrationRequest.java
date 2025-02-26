package com.example.demo.application.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.NICKNAME_REGEXP;

public record CompleteRegistrationRequest(
        @Pattern(regexp = NICKNAME_REGEXP, message = "닉네임 정규식을 맞춰주세요.")
        String nickname,
        @NotBlank(message = "이름은 빈값일 수 없습니다.")
        String name
) {
}
