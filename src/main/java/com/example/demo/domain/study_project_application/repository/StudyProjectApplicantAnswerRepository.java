package com.example.demo.domain.study_project_application.repository;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicantAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyProjectApplicantAnswerRepository extends JpaRepository<StudyProjectApplicantAnswer, Long> {

    @Query("SELECT a FROM StudyProjectApplicantAnswer a " +
            "JOIN FETCH a.studyProjectFormQuestion " +
            "WHERE a.studyProjectApplicant.id = :applicantId")
    Optional<List<StudyProjectApplicantAnswer>> findByStudyProjectApplicant_IdFetchQuestion(Long applicantId);
}
