package com.example.demo.domain.user.domain.dto.request;

import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.NICKNAME_REGEXP;

public record CompleteRegistrationRequest(
        @Pattern(regexp = NICKNAME_REGEXP)
        String nickname
) {
}
