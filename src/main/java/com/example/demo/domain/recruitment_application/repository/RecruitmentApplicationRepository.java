package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.entity.MyRecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RecruitmentApplicationRepository {
    boolean existsByUserIdAndRecruitmentBoardId(Long userId, Long recruitmentBoardId);

    RecruitmentApplicationInfo post(RecruitmentApplicationInfo recruitmentApplicationInfo, Long recruitmentBoardId);

    RecruitmentApplicationInfo patch(RecruitmentApplicationInfo originApplicationInfo, RecruitmentApplicationInfo newApplicationInfo);

    void delete(RecruitmentApplicationInfo recruitmentApplicationInfo);

    Page<RecruitmentApplicationInfo> getPageByRecruitmentBoardIdOrderByCreatedAtDesc(Pageable pageable, Long recruitmentBoardId);

    Page<MyRecruitmentApplicationInfo> getPageByUserIdWithRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType);

    Optional<RecruitmentApplicationInfo> getByIdWithAnswerList(Long applicationId, Long recruitmentBoardId);

    Optional<RecruitmentApplicationInfo> getByIdWithBoard(Long applicationId);

    Optional<RecruitmentApplicationInfo> getById(Long applicationId);
}
