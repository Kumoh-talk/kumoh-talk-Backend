package com.example.demo.domain.recruitment_application.service;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantAnswer;
import com.example.demo.domain.recruitment_application.domain.request.RecruitmentApplicationRequest;
import com.example.demo.domain.recruitment_application.domain.response.MyRecruitmentApplicationPageResponse;
import com.example.demo.domain.recruitment_application.domain.response.RecruitmentApplicantPageResponse;
import com.example.demo.domain.recruitment_application.domain.response.RecruitmentApplicationResponse;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicantAnswerRepository;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicantRepository;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormQuestionRepository;
import com.example.demo.domain.recruitment_board.service.RecruitmentBoardService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
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
public class RecruitmentApplicationService {
    private final UserService userService;
    private final RecruitmentBoardService recruitmentBoardService;

    private final RecruitmentFormQuestionRepository recruitmentFormQuestionRepository;
    private final RecruitmentApplicantRepository recruitmentApplicantRepository;
    private final RecruitmentApplicantAnswerRepository recruitmentApplicantAnswerRepository;

    @Transactional
    public RecruitmentApplicationResponse createApplication(Long userId, Long recruitmentBoardId, RecruitmentApplicationRequest request) {
        User user = userService.validateUser(userId);

        RecruitmentBoard recruitmentBoard = recruitmentBoardService.validateRecruitmentBoard(recruitmentBoardId);
        validateDeadLine(recruitmentBoard);
        validateEssential(recruitmentBoard, request);
        if (recruitmentApplicantRepository.existsByUser_IdAndRecruitmentBoard_Id(userId, recruitmentBoardId)) {
            throw new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_EXIST);
        }

        RecruitmentApplicant applicant = RecruitmentApplicant.from(recruitmentBoard, user);

        List<RecruitmentApplicantAnswer> applicantAnswerList = request.getAnswerList().stream()
                .map(v -> {
                    RecruitmentFormQuestion question = recruitmentFormQuestionRepository.findById(v.getQuestionId())
                            .orElseThrow(() -> new ServiceException(ErrorCode.QUESTION_NOT_FOUND));
                    return RecruitmentApplicantAnswer.from(v, question, applicant);
                })
                .collect(Collectors.toList());

        RecruitmentApplicant saveApplicant = recruitmentApplicantRepository.save(applicant);
        List<RecruitmentApplicantAnswer> saveApplicantAnswerList =
                recruitmentApplicantAnswerRepository.saveAll(applicantAnswerList);

        return RecruitmentApplicationResponse.from(saveApplicant, saveApplicantAnswerList);
    }

    @Transactional(readOnly = true)
    public RecruitmentApplicantPageResponse getApplicantList(
            Long userId,
            Pageable pageable,
            Long recruitmentBoardId,
            boolean isAuthorized) {
        if (!isAuthorized) {
            RecruitmentBoard recruitmentBoard = recruitmentBoardService.validateRecruitmentBoard(recruitmentBoardId);
            if (!recruitmentBoard.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        Page<RecruitmentApplicant> recruitmentApplicantList = recruitmentApplicantRepository.findByRecruitmentBoard_IdOrderByCreatedAtDesc(pageable, recruitmentBoardId);

        return RecruitmentApplicantPageResponse.from(recruitmentApplicantList);
    }

    @Transactional(readOnly = true)
    public RecruitmentApplicationResponse getApplicationInfo(Long userId, Long recruitmentBoardId, Long applicantId, boolean isAuthorized) {
        if (!isAuthorized) {
            RecruitmentBoard recruitmentBoard = recruitmentBoardService.validateRecruitmentBoard(recruitmentBoardId);
            if (!recruitmentBoard.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        RecruitmentApplicant applicant = recruitmentApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        List<RecruitmentApplicantAnswer> recruitmentApplicantAnswerList = recruitmentApplicantAnswerRepository.findByRecruitmentApplicant_IdFetchQuestion(applicantId)
                .orElseGet(ArrayList::new);

        return RecruitmentApplicationResponse.from(applicant, recruitmentApplicantAnswerList);
    }

    @Transactional
    public RecruitmentApplicationResponse updateApplication(Long userId, Long applicantId, RecruitmentApplicationRequest request) {
        userService.validateUser(userId);
        RecruitmentApplicant applicant = recruitmentApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        if (!userId.equals(applicant.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
        validateDeadLine(applicant.getRecruitmentBoard());
        validateEssential(applicant.getRecruitmentBoard(), request);

        List<RecruitmentApplicantAnswer> recruitmentApplicantAnswerList = recruitmentApplicantAnswerRepository.findByRecruitmentApplicant_IdFetchQuestion(applicantId).orElseGet(ArrayList::new);

        System.out.println(recruitmentApplicantAnswerList.size());
        System.out.println(request.getAnswerList().size());

        int idx = 0;
        for (RecruitmentApplicantAnswer applicantAnswer : recruitmentApplicantAnswerList) {
            applicantAnswer.updateFromRequest(request.getAnswerList().get(idx++));
        }
        return RecruitmentApplicationResponse.from(applicant, recruitmentApplicantAnswerList);
    }

    @Transactional
    public void deleteApplication(Long userId, Long applicantId) {
        RecruitmentApplicant applicant = recruitmentApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        if (!userId.equals(applicant.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
        validateDeadLine(applicant.getRecruitmentBoard());
        recruitmentApplicantRepository.deleteById(applicantId);
    }

    @Transactional(readOnly = true)
    public MyRecruitmentApplicationPageResponse getUserApplicationList(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        userService.validateUser(userId);
        Page<RecruitmentApplicant> applicantPage = recruitmentApplicantRepository.findByUser_IdFetchRecruitmentBoard(userId, pageable, boardType);

        return MyRecruitmentApplicationPageResponse.from(applicantPage);
    }

    public void validateDeadLine(RecruitmentBoard recruitmentBoard) {
        if (recruitmentBoard.getRecruitmentDeadline().isBefore(LocalDateTime.now())) {
            throw new ServiceException(ErrorCode.DEADLINE_EXPIRED);
        }
    }

    public void validateEssential(RecruitmentBoard recruitmentBoard, RecruitmentApplicationRequest request) {
        List<RecruitmentApplicationRequest.RecruitmentApplicantAnswerInfoRequest> requestAnswerList = request.getAnswerList();
        for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
            if (recruitmentFormQuestion.getIsEssential()) {
                boolean isBreak = false;
                for (RecruitmentApplicationRequest.RecruitmentApplicantAnswerInfoRequest requestAnswer : requestAnswerList) {
                    if (requestAnswer.getQuestionId().equals(recruitmentFormQuestion.getId())) {
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
