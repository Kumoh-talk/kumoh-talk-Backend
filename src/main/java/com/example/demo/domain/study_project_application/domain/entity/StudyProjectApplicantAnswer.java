package com.example.demo.domain.study_project_application.domain.entity;

import com.example.demo.domain.study_project_application.domain.request.StudyProjectApplicationRequest;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "study_project_applicant_answers")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE study_project_applicant_answers SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class StudyProjectApplicantAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
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

    public static StudyProjectApplicantAnswer from(StudyProjectApplicationRequest.StudyProjectApplicantAnswerInfoRequest request,
                                                   StudyProjectFormQuestion studyProjectFormQuestion,
                                                   StudyProjectApplicant studyProjectApplicant) {
        return StudyProjectApplicantAnswer.builder()
                .answer(request.getAnswer())
                .studyProjectFormQuestion(studyProjectFormQuestion)
                .studyProjectApplicant(studyProjectApplicant)
                .build();
    }

    public void updateFromRequest(StudyProjectApplicationRequest.StudyProjectApplicantAnswerInfoRequest request) {
        this.answer = request.getAnswer();
    }
}
