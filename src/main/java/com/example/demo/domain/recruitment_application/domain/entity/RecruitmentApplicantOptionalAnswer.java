package com.example.demo.domain.recruitment_application.domain.entity;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormAnswer;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "recruitment_applicants_optional_answers")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_applicants_optional_answers SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentApplicantOptionalAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_form_answer_id", nullable = false)
    private RecruitmentFormAnswer recruitmentFormAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_applicant_id", nullable = false)
    private RecruitmentApplicant recruitmentApplicant;

    @Builder
    public RecruitmentApplicantOptionalAnswer(RecruitmentFormAnswer recruitmentFormAnswer, RecruitmentApplicant recruitmentApplicant) {
        this.recruitmentFormAnswer = recruitmentFormAnswer;
        this.recruitmentApplicant = recruitmentApplicant;
    }

    public static RecruitmentApplicantOptionalAnswer from(RecruitmentFormAnswer recruitmentFormAnswer,
                                                          RecruitmentApplicant recruitmentApplicant) {
        return RecruitmentApplicantOptionalAnswer.builder()
                .recruitmentFormAnswer(recruitmentFormAnswer)
                .recruitmentApplicant(recruitmentApplicant)
                .build();
    }
}
