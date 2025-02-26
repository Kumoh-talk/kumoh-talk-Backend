package com.example.demo.application.newsletter.dto.request;

import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record NewsletterUpdateEmailRequest(
        @Pattern(regexp = EMAIL_REGEXP, message = "이메일 정규식을 맞춰주세요.") String email
) {
}
