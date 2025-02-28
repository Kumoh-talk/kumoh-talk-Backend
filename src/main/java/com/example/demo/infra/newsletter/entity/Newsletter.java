package com.example.demo.infra.newsletter.entity;

import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

//    public static Newsletter from(NewsletterSubscribeRequest request) {
//        return Newsletter.builder()
//                .email(request.email())
//                .seminarContentNotice(request.seminarContentNotice())
//                .studyNotice(request.studyNotice())
//                .projectNotice(request.projectNotice())
//                .mentoringNotice(request.mentoringNotice())
//                .build();
//    }

    public void updateNewsletter(String email, Boolean seminarContentNotice, Boolean studyNotice, Boolean projectNotice, Boolean mentoringNotice) {
        this.email = email;
        this.seminarContentNotice = seminarContentNotice;
        this.studyNotice = studyNotice;
        this.projectNotice = projectNotice;
        this.mentoringNotice = mentoringNotice;
    }
}
