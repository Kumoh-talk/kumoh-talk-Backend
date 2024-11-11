package com.example.demo.domain.newsletter.domain;

import com.example.demo.domain.newsletter.domain.dto.request.NewsletterSubscribeRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateEmailRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateNotifyRequest;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "newsletters")
public class Newsletter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private Boolean seminarContentNotice;
    @Column(nullable = false)
    private Boolean studyNotice;
    @Column(nullable = false)
    private Boolean projectNotice;
    @Column(nullable = false)
    private Boolean mentoringNotice;

    @Builder
    public Newsletter(String email, Boolean mentoringNotice, Boolean projectNotice, Boolean seminarContentNotice, Boolean studyNotice) {
        this.email = email;
        this.mentoringNotice = mentoringNotice;
        this.projectNotice = projectNotice;
        this.seminarContentNotice = seminarContentNotice;
        this.studyNotice = studyNotice;
    }

    public static Newsletter from(NewsletterSubscribeRequest request) {
        return Newsletter.builder()
                .email(request.email())
                .seminarContentNotice(request.seminarContentNotice())
                .studyNotice(request.studyNotice())
                .projectNotice(request.projectNotice())
                .mentoringNotice(request.mentoringNotice())
                .build();
    }

    public void updateNewsletterEmail(@Valid NewsletterUpdateEmailRequest request) {
        this.email = request.email();
    }

    public void updateNewsletterNotify(@Valid NewsletterUpdateNotifyRequest request) {
        this.seminarContentNotice = request.seminarContentNotice();
        this.studyNotice = request.studyNotice();
        this.projectNotice = request.projectNotice();
        this.mentoringNotice = request.mentoringNotice();
    }
}
