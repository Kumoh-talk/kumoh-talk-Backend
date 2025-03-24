package com.example.demo.application.newsletter.dto.request;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.example.demo.global.regex.UserRegex.EMAIL_REGEXP;

public record NewsletterUpdateNotifyRequest(
        @Pattern(regexp = EMAIL_REGEXP, message = "이메일 정규식을 맞춰주세요.") String email,
        @NotNull(message = "세미나 내용정리 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean seminarContentNotice,
        @NotNull(message = "스터디 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean studyNotice,
        @NotNull(message = "프로젝트 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean projectNotice,
        @NotNull(message = "멘토링 새 글 업데이트 알림 여/부를 선택해주세요.") Boolean mentoringNotice
) {
    public NewsletterSubscription toNewsletterSubscription() {
        return new NewsletterSubscription(
                this.email,
                this.seminarContentNotice,
                this.studyNotice,
                this.projectNotice,
                this.mentoringNotice
        );
    }
}
