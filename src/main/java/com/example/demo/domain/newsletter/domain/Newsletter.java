package com.example.demo.domain.newsletter.domain;

import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Newsletter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private Boolean isSeminarAnnouncementRequired;
    private Boolean isSeminarContentUpdated;
    private Boolean isStudyUpdated;
    private Boolean isProjectUpdated;

    // TODO. 사용자에 대한 뉴스레터 구독여부를 원하기에 연관관계 고민

    @Builder

    public Newsletter(String email, Boolean isSeminarContentUpdated, Boolean isStudyUpdated, Boolean isProjectUpdated) {
        this.email = email;
        this.isSeminarAnnouncementRequired = true; // 기획상 세미나 활동 공지에 대한 알림은 필수
        this.isSeminarContentUpdated = isSeminarContentUpdated;
        this.isStudyUpdated = isStudyUpdated;
        this.isProjectUpdated = isProjectUpdated;
    }
}
