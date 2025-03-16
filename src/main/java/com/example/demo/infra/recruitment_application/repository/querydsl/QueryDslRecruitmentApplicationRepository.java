package com.example.demo.infra.recruitment_application.repository.querydsl;

import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDslRecruitmentApplicationRepository {
    Page<RecruitmentApplication> findPageByUserIdWithRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType);
}
