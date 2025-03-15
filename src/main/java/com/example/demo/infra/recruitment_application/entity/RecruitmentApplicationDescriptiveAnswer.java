package com.example.demo.infra.recruitment_application.entity;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "recruitment_application_descriptive_answers")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_application_descriptive_answers SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentApplicationDescriptiveAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_form_question_id", nullable = false)
    private RecruitmentFormQuestion recruitmentFormQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_application_id", nullable = false)
    private RecruitmentApplication recruitmentApplication;

    @Builder
    public RecruitmentApplicationDescriptiveAnswer(String answer, RecruitmentFormQuestion recruitmentFormQuestion, RecruitmentApplication recruitmentApplication) {
        this.answer = answer;
        this.recruitmentFormQuestion = recruitmentFormQuestion;
        this.recruitmentApplication = recruitmentApplication;
    }

    public static RecruitmentApplicationDescriptiveAnswer of(
            RecruitmentApplicationAnswerInfo.AnswerInfo answerInfo,
            RecruitmentFormQuestion recruitmentFormQuestion,
            RecruitmentApplication recruitmentApplication) {
        return RecruitmentApplicationDescriptiveAnswer.builder()
                .answer(answerInfo.getAnswer())
                .recruitmentFormQuestion(recruitmentFormQuestion)
                .recruitmentApplication(recruitmentApplication)
                .build();
    }

    public RecruitmentApplicationAnswerInfo.AnswerInfo toDomain() {
        return RecruitmentApplicationAnswerInfo.AnswerInfo.builder()
                .answerId(id)
                .answerNumber(null)
                .answer(answer)
                .build();
    }

    public void update(RecruitmentApplicationAnswerInfo.AnswerInfo answerInfo) {
        this.answer = answerInfo.getAnswer();
    }
}
