package com.example.demo.domain.study_project_application.service;

import com.example.demo.domain.study_project_application.repository.StudyProjectApplicantRepository;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyProjectApplicationService {
    private final StudyProjectFormQuestionRepository studyProjectFormQuestionRepository;
    private final StudyProjectApplicantRepository studyProjectApplicantRepository;
}
