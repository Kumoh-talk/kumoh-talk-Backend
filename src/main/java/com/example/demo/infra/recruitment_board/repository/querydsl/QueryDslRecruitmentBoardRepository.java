package com.example.demo.infra.recruitment_board.repository.querydsl;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QueryDslRecruitmentBoardRepository {
    List<RecruitmentBoardInfo> findPublishedPageByNoOffset(int size, RecruitmentBoardInfo lastBoardInfo, RecruitmentBoardType boardType);

    List<RecruitmentBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId);

    Page<RecruitmentBoardInfo> findPublishedPageByPageNum(Long userId, Pageable pageable, RecruitmentBoardType boardType);

    Optional<RecruitmentBoard> findByIdByFetchingQuestionList(Long recruitmentBoardId);

}
