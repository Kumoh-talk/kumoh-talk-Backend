package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QueryDslRecruitmentBoardRepository {
    List<RecruitmentBoard> findPublishedPageByNoOffset(int size, RecruitmentBoard lastBoard, RecruitmentBoardType boardType, boolean isFirst);

    Page<RecruitmentBoard> findPublishedPageByPageNum(Pageable pageable, RecruitmentBoardType boardType);

    Page<RecruitmentBoard> findPublishedPageByUserIdByPageNum(Long userId, Pageable pageable, RecruitmentBoardType boardType);

    List<RecruitmentBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId, boolean isFirst);

    Optional<RecruitmentBoard> findByIdByFetchingChoiceAnswerList(Long recruitmentBoardId);
}
