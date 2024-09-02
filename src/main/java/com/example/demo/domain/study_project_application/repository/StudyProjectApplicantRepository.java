package com.example.demo.domain.study_project_application.repository;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyProjectApplicantRepository extends JpaRepository<StudyProjectApplicant, Long> {
    Optional<List<StudyProjectApplicant>> findByStudyProjectBoard_Id(Long studyProjectBoard_id);

    Optional<StudyProjectApplicant> findByUser_IdAndStudyProjectBoard_Id(Long userId, Long studyProjectBoardId);

    Page<StudyProjectApplicant> findByStudyProjectBoard_IdOrderByCreatedAtDesc(Pageable pageable, Long studyProjectBoard_id);
}
