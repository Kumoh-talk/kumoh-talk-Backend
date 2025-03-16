package com.example.demo.domain.recruitment_board.implement.board;

import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.recruitment_application.repository.jpa.RecruitmentApplicationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RecruitmentBoardValidator {
    private final RecruitmentApplicationJpaRepository recruitmentApplicantRepository;

    public boolean isPublished(RecruitmentBoardInfo recruitmentBoardInfo) {
        return recruitmentBoardInfo.getStatus() == Status.PUBLISHED;
    }

    public boolean isStatusChangedToPublished(RecruitmentBoardInfo originBoardInfo, RecruitmentBoardInfo newBoardInfo) {
        return originBoardInfo.getStatus() == Status.DRAFT && newBoardInfo.getStatus() == Status.PUBLISHED;
    }

    public void validatePatchable(RecruitmentBoardInfo recruitmentBoardInfo) {
        // 신청자가 존재하면 수정 불가
        if (recruitmentApplicantRepository.existsByRecruitmentBoardId(recruitmentBoardInfo.getBoardId())) {
            throw new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_EXIST);
        }
    }

    public void validateWriter(Long userId, RecruitmentBoardInfo recruitmentBoardInfo) {
        if (userId == null || !userId.equals(recruitmentBoardInfo.getUserId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
    }

    public void validateAccessToBoard(Long userId, RecruitmentBoardInfo recruitmentBoardInfo) {
        if (!isPublished(recruitmentBoardInfo)) {
            try {
                validateWriter(userId, recruitmentBoardInfo);
            } catch (ServiceException e) {
                throw new ServiceException(ErrorCode.DRAFT_NOT_ACCESS_USER);
            }
        }
    }

    public void validateDeadLine(Long userId, RecruitmentBoardInfo recruitmentBoardInfo) {
        if (recruitmentBoardInfo.getRecruitmentDeadline().isBefore(LocalDateTime.now())) {
            try {
                validateWriter(userId, recruitmentBoardInfo);
            } catch (ServiceException e) {
                throw new ServiceException(ErrorCode.DEADLINE_EXPIRED);
            }
        }
    }
}
