package com.example.demo.application.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.S3UrlRegex.S3_PROFILE_FILE_URL;

public record ChangeProfileUrlRequest (
        // @Pattern(regexp = S3_PROFILE_FILE_URL) TODO : S3 URL
        @NotBlank(message = "파일 url은 필수 입니다.")
        String url
) {
}
