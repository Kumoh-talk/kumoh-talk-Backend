package com.example.demo.domain.newsletter.domain;

import com.example.demo.domain.newsletter.domain.dto.request.NewsletterSubscribeRequest;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
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
@SQLDelete(sql = "UPDATE newsletters SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class Newsletter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Boolean isSeminarAnnouncementRequired;
    @Column(nullable = false)
    private Boolean isSeminarContentUpdated;
    @Column(nullable = false)
    private Boolean isStudyUpdated;
    @Column(nullable = false)
    private Boolean isProjectUpdated;

    @Builder
    public Newsletter(String email, Boolean isSeminarContentUpdated, Boolean isStudyUpdated, Boolean isProjectUpdated) {
        this.email = email;
        this.isSeminarAnnouncementRequired = true; // 기획상 세미나 활동 공지에 대한 알림은 필수
        this.isSeminarContentUpdated = isSeminarContentUpdated;
        this.isStudyUpdated = isStudyUpdated;
        this.isProjectUpdated = isProjectUpdated;
    }

    public static Newsletter from(NewsletterSubscribeRequest request) {
        return Newsletter.builder()
                .email(request.email())
                .isSeminarContentUpdated(request.isSeminarContentUpdated())
                .isStudyUpdated(request.isStudyUpdated())
                .isProjectUpdated(request.isProjectUpdated())
                .build();
    }
}
