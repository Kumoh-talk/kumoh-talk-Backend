package com.example.demo.domain.recruitment_application.implement;

import com.example.demo.domain.recruitment_application.entity.MyRecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicationRepository;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecruitmentApplicationReader {
    private final RecruitmentApplicationRepository recruitmentApplicationRepository;

    public boolean existsByUserIdAndRecruitmentBoardId(Long userId, Long recruitmentBoardId) {
        return recruitmentApplicationRepository.existsByUserIdAndRecruitmentBoardId(userId, recruitmentBoardId);
    }

    public Page<RecruitmentApplicationInfo> getPageByRecruitmentBoardIdOrderByCreatedAtDesc(Pageable pageable, Long recruitmentBoardId) {
        return recruitmentApplicationRepository.getPageByRecruitmentBoardIdOrderByCreatedAtDesc(pageable, recruitmentBoardId);
    }

    public Page<MyRecruitmentApplicationInfo> getPageByUserIdWithRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        return recruitmentApplicationRepository.getPageByUserIdWithRecruitmentBoard(userId, pageable, boardType);
    }

    public Optional<RecruitmentApplicationInfo> getByIdWithAnswerList(Long applicationId, Long recruitmentBoardId) {
        return recruitmentApplicationRepository.getByIdWithAnswerList(applicationId, recruitmentBoardId);
    }

    public Optional<RecruitmentApplicationInfo> getById(Long applicationId) {
        return recruitmentApplicationRepository.getById(applicationId);
    }

    public Optional<RecruitmentApplicationInfo> getByIdWithBoard(Long applicationId) {
        return recruitmentApplicationRepository.getByIdWithBoard(applicationId);
    }
}
