package com.example.demo.domain.recruitment_application.domain.entity;

import com.example.demo.domain.recruitment_application.domain.request.RecruitmentApplicationRequest;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "recruitment_applicants_answers")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_applicants_answers SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentApplicantAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_form_question_id", nullable = false)
    private RecruitmentFormQuestion recruitmentFormQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_applicant_id", nullable = false)
    private RecruitmentApplicant recruitmentApplicant;

    @Builder
    public RecruitmentApplicantAnswer(String answer, RecruitmentFormQuestion recruitmentFormQuestion, RecruitmentApplicant recruitmentApplicant) {
        this.answer = answer;
        this.recruitmentFormQuestion = recruitmentFormQuestion;
        this.recruitmentApplicant = recruitmentApplicant;
    }

    public static RecruitmentApplicantAnswer from(RecruitmentApplicationRequest.RecruitmentApplicantAnswerInfoRequest request,
                                                  RecruitmentFormQuestion recruitmentFormQuestion,
                                                  RecruitmentApplicant recruitmentApplicant) {
        return RecruitmentApplicantAnswer.builder()
                .answer(request.getAnswer())
                .recruitmentFormQuestion(recruitmentFormQuestion)
                .recruitmentApplicant(recruitmentApplicant)
                .build();
    }

    public void updateFromRequest(RecruitmentApplicationRequest.RecruitmentApplicantAnswerInfoRequest request) {
        this.answer = request.getAnswer();
    }
}
