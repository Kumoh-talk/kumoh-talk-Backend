package com.example.demo.domain.study_project_application.service;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicantAnswer;
import com.example.demo.domain.study_project_application.domain.request.StudyProjectApplicationRequest;
import com.example.demo.domain.study_project_application.domain.response.MyStudyProjectApplicationPageResponse;
import com.example.demo.domain.study_project_application.domain.response.StudyProjectApplicantPageResponse;
import com.example.demo.domain.study_project_application.domain.response.StudyProjectApplicationResponse;
import com.example.demo.domain.study_project_application.repository.StudyProjectApplicantAnswerRepository;
import com.example.demo.domain.study_project_application.repository.StudyProjectApplicantRepository;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormQuestionRepository;
import com.example.demo.domain.study_project_board.service.StudyProjectBoardService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyProjectApplicationService {
    private final UserService userService;
    private final UserAdditionalInfoService userAdditionalInfoService;
    private final StudyProjectBoardService studyProjectBoardService;

    private final StudyProjectFormQuestionRepository studyProjectFormQuestionRepository;
    private final StudyProjectApplicantRepository studyProjectApplicantRepository;
    private final StudyProjectApplicantAnswerRepository studyProjectApplicantAnswerRepository;

    @Transactional
    public StudyProjectApplicationResponse createApplication(Long userId, Long studyProjectBoardId, StudyProjectApplicationRequest request) {
        User user = userService.validateUser(userId);

        StudyProjectBoard studyProjectBoard = studyProjectBoardService.validateStudyProjectBoard(studyProjectBoardId);
        validateDeadLine(studyProjectBoard);
        validateEssential(studyProjectBoard, request);
        if (studyProjectApplicantRepository.existsByUser_IdAndStudyProjectBoard_Id(userId, studyProjectBoardId)) {
            throw new ServiceException(ErrorCode.STUDYPROJECT_APPLICANT_EXIST);
        }

        StudyProjectApplicant applicant = StudyProjectApplicant.from(studyProjectBoard, user);

        List<StudyProjectApplicantAnswer> applicantAnswerList = request.getAnswerList().stream()
                .map(v -> {
                    StudyProjectFormQuestion question = studyProjectFormQuestionRepository.findById(v.getQuestionId())
                            .orElseThrow(() -> new ServiceException(ErrorCode.QUESTION_NOT_FOUND));
                    return StudyProjectApplicantAnswer.from(v, question, applicant);
                })
                .collect(Collectors.toList());

        StudyProjectApplicant saveApplicant = studyProjectApplicantRepository.save(applicant);
        List<StudyProjectApplicantAnswer> saveApplicantAnswerList =
                studyProjectApplicantAnswerRepository.saveAll(applicantAnswerList);

        return StudyProjectApplicationResponse.from(saveApplicant, saveApplicantAnswerList);
    }

    @Transactional(readOnly = true)
    public StudyProjectApplicantPageResponse getApplicantList(
            Long userId,
            Pageable pageable,
            Long studyProjectBoardId) {
        Role userRole = userService.validateUser(userId).getRole();

        StudyProjectBoard studyProjectBoard = studyProjectBoardService.validateStudyProjectBoard(studyProjectBoardId);
        if (!studyProjectBoard.getUser().getId().equals(userId) && userRole != Role.ROLE_ADMIN) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        Page<StudyProjectApplicant> studyProjectApplicantList = studyProjectApplicantRepository.findByStudyProjectBoard_IdOrderByCreatedAtDesc(pageable, studyProjectBoardId);

        return StudyProjectApplicantPageResponse.from(studyProjectApplicantList);
    }

    @Transactional(readOnly = true)
    public StudyProjectApplicationResponse getApplicationInfo(Long userId, Long studyProjectBoardId, Long applicantId) {
        Role userRole = userService.validateUser(userId).getRole();

        StudyProjectBoard studyProjectBoard = studyProjectBoardService.validateStudyProjectBoard(studyProjectBoardId);
        if (!studyProjectBoard.getUser().getId().equals(userId) && userRole != Role.ROLE_ADMIN) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        StudyProjectApplicant applicant = studyProjectApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.STUDYPROJECT_APPLICANT_NOT_FOUND));
        List<StudyProjectApplicantAnswer> studyProjectApplicantAnswerList = studyProjectApplicantAnswerRepository.findByStudyProjectApplicant_IdFetchQuestion(applicantId)
                .orElseGet(ArrayList::new);

        return StudyProjectApplicationResponse.from(applicant, studyProjectApplicantAnswerList);
    }

    @Transactional
    public StudyProjectApplicationResponse updateApplication(Long userId, Long applicantId, StudyProjectApplicationRequest request) {
        userService.validateUser(userId);
        StudyProjectApplicant applicant = studyProjectApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.STUDYPROJECT_APPLICANT_NOT_FOUND));
        if (!userId.equals(applicant.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
        validateDeadLine(applicant.getStudyProjectBoard());
        validateEssential(applicant.getStudyProjectBoard(), request);

        List<StudyProjectApplicantAnswer> studyProjectApplicantAnswerList = studyProjectApplicantAnswerRepository.findByStudyProjectApplicant_IdFetchQuestion(applicantId).orElseGet(ArrayList::new);

        System.out.println(studyProjectApplicantAnswerList.size());
        System.out.println(request.getAnswerList().size());

        int idx = 0;
        for (StudyProjectApplicantAnswer applicantAnswer : studyProjectApplicantAnswerList) {
            applicantAnswer.updateFromRequest(request.getAnswerList().get(idx++));
        }
        return StudyProjectApplicationResponse.from(applicant, studyProjectApplicantAnswerList);
    }

    @Transactional
    public void deleteApplication(Long userId, Long applicantId) {
        userService.validateUser(userId);
        StudyProjectApplicant applicant = studyProjectApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.STUDYPROJECT_APPLICANT_NOT_FOUND));
        if (!userId.equals(applicant.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
        validateDeadLine(applicant.getStudyProjectBoard());
        studyProjectApplicantRepository.deleteById(applicantId);
    }

    @Transactional(readOnly = true)
    public MyStudyProjectApplicationPageResponse getUserApplicationList(Long userId, Pageable pageable, StudyProjectBoardType boardType) {
        userService.validateUser(userId);
        Page<StudyProjectApplicant> applicantPage = studyProjectApplicantRepository.findByUser_IdFetchStudyProjectBoard(userId, pageable, boardType);

        return MyStudyProjectApplicationPageResponse.from(applicantPage);
    }

    public void validateDeadLine(StudyProjectBoard studyProjectBoard) {
        if (studyProjectBoard.getRecruitmentDeadline().isBefore(LocalDateTime.now())) {
            throw new ServiceException(ErrorCode.DEADLINE_EXPIRED);
        }
    }

    public void validateEssential(StudyProjectBoard studyProjectBoard, StudyProjectApplicationRequest request) {
        List<StudyProjectApplicationRequest.StudyProjectApplicantAnswerInfoRequest> requestAnswerList = request.getAnswerList();
        for (StudyProjectFormQuestion studyProjectFormQuestion : studyProjectBoard.getStudyProjectFormQuestionList()) {
            if (studyProjectFormQuestion.getIsEssential()) {
                boolean isBreak = false;
                for (StudyProjectApplicationRequest.StudyProjectApplicantAnswerInfoRequest requestAnswer : requestAnswerList) {
                    if (requestAnswer.getQuestionId().equals(studyProjectFormQuestion.getId())) {
                        isBreak = true;
                        break;
                    }
                }
                if (!isBreak) {
                    throw new ServiceException(ErrorCode.OMIT_ESSENTIAL_QUESTION);
                }
            }
        }

    }
}
