package com.example.demo.domain.recruitment_application.domain.entity;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recruitment_applicants")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_applicants SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentApplicant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_board_id", nullable = false)
    private RecruitmentBoard recruitmentBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recruitmentApplicant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecruitmentApplicantAnswer> answerList = new ArrayList<>();

    @Builder
    public RecruitmentApplicant(RecruitmentBoard recruitmentBoard, User user) {
        this.recruitmentBoard = recruitmentBoard;
        this.user = user;
    }

    public static RecruitmentApplicant from(RecruitmentBoard recruitmentBoard, User user) {
        return RecruitmentApplicant.builder()
                .recruitmentBoard(recruitmentBoard)
                .user(user)
                .build();
    }
}
