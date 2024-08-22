package com.example.demo.domain.user.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileImageRequest(
        @NotBlank
        String profileImage
) {
}
