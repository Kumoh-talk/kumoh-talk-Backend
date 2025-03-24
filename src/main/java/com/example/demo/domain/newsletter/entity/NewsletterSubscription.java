package com.example.demo.domain.newsletter.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsletterSubscription {
    private final String email;
    private final Boolean seminarContentNotice;
    private final Boolean studyNotice;
    private final Boolean projectNotice;
    private final Boolean mentoringNotice;

    @Builder
    public NewsletterSubscription(String email, Boolean seminarContentNotice, Boolean studyNotice, Boolean projectNotice, Boolean mentoringNotice) {
        this.email = email;
        this.seminarContentNotice = seminarContentNotice;
        this.studyNotice = studyNotice;
        this.projectNotice = projectNotice;
        this.mentoringNotice = mentoringNotice;
    }
}
