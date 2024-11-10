package com.example.demo.domain.recruitment_board.domain.entity;

import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentFormChoiceAnswerRequest;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "recruitment_form_choice_answers")
@NoArgsConstructor
@Getter
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentFormChoiceAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false, length = 50)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_form_question_id", nullable = false)
    private RecruitmentFormQuestion recruitmentFormQuestion;

    @Builder
    public RecruitmentFormChoiceAnswer(int number, String answer, RecruitmentFormQuestion recruitmentFormQuestion) {
        this.number = number;
        this.answer = answer;
        this.recruitmentFormQuestion = recruitmentFormQuestion;
    }

    public static RecruitmentFormChoiceAnswer from(
            RecruitmentFormChoiceAnswerRequest choiceAnswerRequest,
            RecruitmentFormQuestion recruitmentFormQuestion) {
        return RecruitmentFormChoiceAnswer.builder()
                .number(choiceAnswerRequest.getNumber())
                .answer(choiceAnswerRequest.getAnswer())
                .recruitmentFormQuestion(recruitmentFormQuestion)
                .build();
    }

    public void updateFromRequest(RecruitmentFormChoiceAnswerRequest recruitmentFormChoiceAnswerRequest) {
        this.number = recruitmentFormChoiceAnswerRequest.getNumber();
        this.answer = recruitmentFormChoiceAnswerRequest.getAnswer();
    }
}
