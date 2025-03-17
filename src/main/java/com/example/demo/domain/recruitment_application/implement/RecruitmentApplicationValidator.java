package com.example.demo.domain.recruitment_application.implement;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_board.implement.form.RecruitmentFormQuestionReader;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RecruitmentApplicationValidator {
    private final RecruitmentApplicationReader recruitmentApplicationReader;
    private final RecruitmentFormQuestionReader recruitmentFormQuestionReader;

    // 필수 질문 답변 여부 확인
    public void validateEssential(Long recruitmentBoardId, RecruitmentApplicationInfo recruitmentApplicationInfo) {
        List<Long> essentialQuestionIdList = recruitmentFormQuestionReader.getEssentialListByRecruitmentBoardId(recruitmentBoardId);

        Set<Long> essentialQuestionIds = new HashSet<>(essentialQuestionIdList);

        for (RecruitmentApplicationAnswerInfo answerInfo : recruitmentApplicationInfo.getAnswerInfoList()) {
            if (essentialQuestionIds.contains(answerInfo.getQuestionId())) {
                if (answerInfo.getAnswerInfoList() == null || answerInfo.getAnswerInfoList().isEmpty()) {
                    throw new ServiceException(ErrorCode.OMIT_ESSENTIAL_QUESTION);
                }
            }
        }
    }

    public void validateWriter(Long userId, RecruitmentApplicationInfo recruitmentApplicationInfo) {
        if (userId == null || !userId.equals(recruitmentApplicationInfo.getUserId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
    }

    public void validateExist(Long userId, Long recruitmentBoardId) {
        if (recruitmentApplicationReader.existsByUserIdAndRecruitmentBoardId(userId, recruitmentBoardId)) {
            throw new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_EXIST);
        }
    }
}
