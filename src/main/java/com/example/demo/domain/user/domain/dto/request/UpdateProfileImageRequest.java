package com.example.demo.domain.user.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileImageRequest(
        @NotBlank(message = "이미지는 빈값일 수 없습니다.")
        String profileImage
) {
}
