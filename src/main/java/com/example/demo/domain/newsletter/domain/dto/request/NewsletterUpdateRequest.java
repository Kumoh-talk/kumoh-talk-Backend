package com.example.demo.domain.newsletter.domain.dto.request;

import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record NewsletterUpdateRequest(
        @Pattern(regexp = EMAIL_REGEXP)
        String email,
        Boolean isSeminarContentUpdated,
        Boolean isStudyUpdated,
        Boolean isProjectUpdated
) {
}
