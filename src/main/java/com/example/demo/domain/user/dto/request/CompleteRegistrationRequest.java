package com.example.demo.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CompleteRegistrationRequest(
        @NotBlank(message = "닉네임은 빈 값일 수 없습니다.")
        String nickname
) {
}
