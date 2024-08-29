package com.example.demo.domain.study_project_board.domain.entity;

import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectFormChoiceAnswerRequest;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "study_project_form_choice_answers")
@NoArgsConstructor
@Getter
@SQLRestriction(value = "deleted_at is NULL")
public class StudyProjectFormChoiceAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_form_question_id", nullable = false)
    private StudyProjectFormQuestion studyProjectFormQuestion;

    @Builder
    public StudyProjectFormChoiceAnswer(int number, String answer, StudyProjectFormQuestion studyProjectFormQuestion) {
        this.number = number;
        this.answer = answer;
        this.studyProjectFormQuestion = studyProjectFormQuestion;
    }

    public static StudyProjectFormChoiceAnswer from(
            StudyProjectFormChoiceAnswerRequest choiceAnswerRequest,
            StudyProjectFormQuestion studyProjectFormQuestion) {
        return StudyProjectFormChoiceAnswer.builder()
                .number(choiceAnswerRequest.getNumber())
                .answer(choiceAnswerRequest.getAnswer())
                .studyProjectFormQuestion(studyProjectFormQuestion)
                .build();
    }

    public void updateFromRequest(StudyProjectFormChoiceAnswerRequest studyProjectFormChoiceAnswerRequest) {
        this.number = studyProjectFormChoiceAnswerRequest.getNumber();
        this.answer = studyProjectFormChoiceAnswerRequest.getAnswer();
    }
}
