package com.example.demo.application.user.dto.request;

import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.NICKNAME_REGEXP;

public record UpdateNicknameRequest(
        @Pattern(regexp = NICKNAME_REGEXP, message = "닉네임 정규식을 맞춰주세요.")
        String nickname
) {
}
