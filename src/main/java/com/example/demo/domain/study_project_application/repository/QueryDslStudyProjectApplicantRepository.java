package com.example.demo.domain.study_project_application.repository;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDslStudyProjectApplicantRepository {
    Page<StudyProjectApplicant> findByUser_IdFetchStudyProjectBoard(Long userId, Pageable pageable, StudyProjectBoardType boardType);
}
