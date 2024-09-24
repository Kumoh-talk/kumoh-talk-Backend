package com.example.demo.domain.user.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.S3UrlRegex.S3_PROFILE_FILE_URL;

public record ChangeProfileUrlRequest (
        @NotBlank(message = "파일 url은 필수 입니다.")
        @Pattern(regexp = S3_PROFILE_FILE_URL)
        String url
) {
}
