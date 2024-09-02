package com.example.demo.domain.study_project_application.domain.entity;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_project_applicant_answers")
@NoArgsConstructor
@Getter
public class StudyProjectApplicantAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_form_question_id", nullable = false)
    private StudyProjectFormQuestion studyProjectFormQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_applicant_id", nullable = false)
    private StudyProjectApplicant studyProjectApplicant;

    @Builder
    public StudyProjectApplicantAnswer(String answer, StudyProjectFormQuestion studyProjectFormQuestion, StudyProjectApplicant studyProjectApplicant) {
        this.answer = answer;
        this.studyProjectFormQuestion = studyProjectFormQuestion;
        this.studyProjectApplicant = studyProjectApplicant;
    }
}
