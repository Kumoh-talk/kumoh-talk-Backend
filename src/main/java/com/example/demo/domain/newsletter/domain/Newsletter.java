package com.example.demo.domain.newsletter.domain;

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
@SQLDelete(sql = "UPDATE newsletter SET deleted_at = NOW() where id=?")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Newsletter(String email, Boolean isSeminarContentUpdated, Boolean isStudyUpdated, Boolean isProjectUpdated, User user) {
        this.email = email;
        this.isSeminarAnnouncementRequired = true; // 기획상 세미나 활동 공지에 대한 알림은 필수
        this.isSeminarContentUpdated = isSeminarContentUpdated;
        this.isStudyUpdated = isStudyUpdated;
        this.isProjectUpdated = isProjectUpdated;
        this.user = user;
    }
}
