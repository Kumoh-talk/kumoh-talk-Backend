package com.example.demo.infra.recruitment_application.entity;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormAnswer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "recruitment_application_optional_answers")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_application_optional_answers SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentApplicationOptionalAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_form_answer_id", nullable = false)
    private RecruitmentFormAnswer recruitmentFormAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_application_id", nullable = false)
    private RecruitmentApplication recruitmentApplication;

    @Builder
    public RecruitmentApplicationOptionalAnswer(RecruitmentFormAnswer recruitmentFormAnswer, RecruitmentApplication recruitmentApplication) {
        this.recruitmentFormAnswer = recruitmentFormAnswer;
        this.recruitmentApplication = recruitmentApplication;
    }

    public static RecruitmentApplicationOptionalAnswer of(
            RecruitmentFormAnswer recruitmentFormAnswer,
            RecruitmentApplication recruitmentApplication) {
        return RecruitmentApplicationOptionalAnswer.builder()
                .recruitmentFormAnswer(recruitmentFormAnswer)
                .recruitmentApplication(recruitmentApplication)
                .build();
    }

    public RecruitmentApplicationAnswerInfo.AnswerInfo toDomain() {
        return RecruitmentApplicationAnswerInfo.AnswerInfo.builder()
                .answerId(recruitmentFormAnswer.getId())
                .answerNumber(recruitmentFormAnswer.getNumber())
                .answer(recruitmentFormAnswer.getAnswer())
                .build();
    }
}
