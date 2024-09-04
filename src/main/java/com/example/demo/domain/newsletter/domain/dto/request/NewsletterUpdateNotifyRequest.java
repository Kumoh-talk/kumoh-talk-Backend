package com.example.demo.domain.newsletter.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record NewsletterUpdateNotifyRequest(
        @NotNull(message = "세미나 내용정리 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean isSeminarContentUpdated,
        @NotNull(message = "스터디 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean isStudyUpdated,
        @NotNull(message = "프로젝트 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean isProjectUpdated
) {
}
