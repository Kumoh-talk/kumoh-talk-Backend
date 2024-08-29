package com.example.demo.domain.study_project_application.repository;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyProjectApplicantRepository extends JpaRepository<StudyProjectApplicant, Long> {
}
