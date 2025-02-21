package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.dto.response.RecruitmentBoardNoOffsetResponse;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QueryDslRecruitmentBoardRepository {
    List<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> findPublishedPageByNoOffset(int size, RecruitmentBoard lastBoard, RecruitmentBoardType boardType);

    Page<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> findPublishedPageByPageNum(Pageable pageable, RecruitmentBoardType boardType);

    Page<RecruitmentBoardNoOffsetResponse.RecruitmentBoardSummaryInfo> findPublishedPageByUserIdByPageNum(Long userId, Pageable pageable, RecruitmentBoardType boardType);

    List<RecruitmentBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId);

    Optional<RecruitmentBoard> findByIdByFetchingQuestionList(Long recruitmentBoardId);
}
