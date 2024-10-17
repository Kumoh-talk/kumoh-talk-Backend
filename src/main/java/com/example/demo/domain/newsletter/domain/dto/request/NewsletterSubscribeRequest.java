package com.example.demo.domain.newsletter.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record NewsletterSubscribeRequest(
        @Pattern(regexp = EMAIL_REGEXP, message = "이메일 정규식을 맞춰주세요.") String email,
        @NotNull(message = "세미나 내용정리 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean isSeminarContentUpdated,
        @NotNull(message = "스터디 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean isStudyUpdated,
        @NotNull(message = "프로젝트 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean isProjectUpdated
) {
}
