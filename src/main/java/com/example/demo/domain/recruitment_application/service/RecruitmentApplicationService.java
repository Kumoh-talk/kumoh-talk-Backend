package com.example.demo.domain.recruitment_application.service;

import com.example.demo.domain.recruitment_application.entity.MyRecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.implement.RecruitmentApplicationReader;
import com.example.demo.domain.recruitment_application.implement.RecruitmentApplicationValidator;
import com.example.demo.domain.recruitment_application.implement.RecruitmentApplicationWriter;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.implement.board.RecruitmentBoardReader;
import com.example.demo.domain.recruitment_board.implement.board.RecruitmentBoardValidator;
import com.example.demo.domain.recruitment_board.implement.board.RecruitmentBoardWriter;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitmentApplicationService {
    private final UserReader userReader;
    private final RecruitmentBoardReader recruitmentBoardReader;
    private final RecruitmentBoardWriter recruitmentBoardWriter;
    private final RecruitmentBoardValidator recruitmentBoardValidator;

    private final RecruitmentApplicationReader recruitmentApplicationReader;
    private final RecruitmentApplicationWriter recruitmentApplicationWriter;
    private final RecruitmentApplicationValidator recruitmentApplicationValidator;

    @Transactional
    public RecruitmentApplicationInfo postApplication(RecruitmentApplicationInfo recruitmentApplicationInfo) {
        userReader.findUser(recruitmentApplicationInfo.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        RecruitmentBoardInfo recruitmentBoardInfo = recruitmentBoardReader.getByIdByWithQuestionList(recruitmentApplicationInfo.getRecruitmentBoardId())
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        recruitmentBoardValidator.validateExpired(recruitmentApplicationInfo.getUserId(), recruitmentBoardInfo);
        recruitmentApplicationValidator.validateEssential(recruitmentApplicationInfo.getRecruitmentBoardId(), recruitmentApplicationInfo);
        recruitmentApplicationValidator.validateExist(recruitmentApplicationInfo.getUserId(), recruitmentApplicationInfo.getRecruitmentBoardId());

        try {
            RecruitmentApplicationInfo applicationInfo = recruitmentApplicationWriter.post(recruitmentApplicationInfo, recruitmentApplicationInfo.getRecruitmentBoardId());
            recruitmentBoardWriter.increaseCurrentMemberNum(recruitmentApplicationInfo.getRecruitmentBoardId());
            return applicationInfo;
        } catch (InvalidDataAccessApiUsageException e) {
            if (e.getMessage().equals("question")) {
                throw new ServiceException(ErrorCode.QUESTION_NOT_FOUND);
            } else {
                throw new ServiceException(ErrorCode.ANSWER_NOT_FOUND);
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<RecruitmentApplicationInfo> getApplicationList(
            Long userId,
            Pageable pageable,
            Long recruitmentBoardId,
            boolean isAuthorized) {
        RecruitmentBoardInfo recruitmentBoardInfo = recruitmentBoardReader.getById(recruitmentBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        if (!isAuthorized) {
            if (!recruitmentBoardInfo.getUserId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        return recruitmentApplicationReader.getPageByRecruitmentBoardIdOrderByCreatedAtDesc(pageable, recruitmentBoardId);

    }

    @Transactional(readOnly = true)
    public RecruitmentApplicationInfo getApplicationInfo(Long userId, Long recruitmentBoardId, Long applicationId, boolean isAuthorized) {
        RecruitmentBoardInfo recruitmentBoardInfo = recruitmentBoardReader.getByIdByWithQuestionList(recruitmentBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        if (!isAuthorized) {
            if (!recruitmentBoardInfo.getUserId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }
        return recruitmentApplicationReader.getByIdWithAnswerList(applicationId, recruitmentBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
    }

    @Transactional
    public RecruitmentApplicationInfo patchApplication(RecruitmentApplicationInfo recruitmentApplicationInfo) {
        userReader.findUser(recruitmentApplicationInfo.getUserId())
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        RecruitmentApplicationInfo originApplicationInfo = recruitmentApplicationReader.getByIdWithBoard(recruitmentApplicationInfo.getApplicationId())
                .orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));

        recruitmentApplicationValidator.validateWriter(recruitmentApplicationInfo.getUserId(), originApplicationInfo);

        RecruitmentBoardInfo recruitmentBoardInfo = recruitmentBoardReader.getByIdByWithQuestionList(originApplicationInfo.getRecruitmentBoardId())
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        recruitmentBoardValidator.validateExpired(recruitmentApplicationInfo.getUserId(), recruitmentBoardInfo);
        recruitmentApplicationValidator.validateEssential(originApplicationInfo.getRecruitmentBoardId(), recruitmentApplicationInfo);

        try {
            return recruitmentApplicationWriter.patch(originApplicationInfo, recruitmentApplicationInfo);
        } catch (InvalidDataAccessApiUsageException e) {
            if (e.getMessage().equals("question")) {
                throw new ServiceException(ErrorCode.QUESTION_NOT_FOUND);
            } else {
                throw new ServiceException(ErrorCode.ANSWER_NOT_FOUND);
            }
        }
    }

    @Transactional
    public void deleteApplication(Long userId, Long applicationId) {
        RecruitmentApplicationInfo applicationInfo = recruitmentApplicationReader.getByIdWithBoard(applicationId)
                .orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        RecruitmentBoardInfo boardInfo = recruitmentBoardReader.getByIdWithLock(applicationInfo.getRecruitmentBoardId())
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        recruitmentApplicationValidator.validateWriter(userId, applicationInfo);
        recruitmentBoardValidator.validateExpired(userId, boardInfo);

        recruitmentApplicationWriter.delete(applicationInfo);
        recruitmentBoardWriter.decreaseCurrentMemberNum(applicationInfo.getRecruitmentBoardId());
    }

    @Transactional(readOnly = true)
    public Page<MyRecruitmentApplicationInfo> getUserApplicationList(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        userReader.findUser(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        return recruitmentApplicationReader.getPageByUserIdWithRecruitmentBoard(userId, pageable, boardType);
    }
}
