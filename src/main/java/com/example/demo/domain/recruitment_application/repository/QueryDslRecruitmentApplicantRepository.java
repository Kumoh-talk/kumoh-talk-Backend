package com.example.demo.domain.recruitment_application.repository;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDslRecruitmentApplicantRepository {
    Page<RecruitmentApplicant> findByUser_IdFetchRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType);
}
