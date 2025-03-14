package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardAndFormInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecruitmentBoardRepository {
    RecruitmentBoardAndFormInfo save(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo);

    Optional<RecruitmentBoardInfo> getById(Long boardId);

    Optional<RecruitmentBoardInfo> getByIdWithUser(Long boardId);

    Optional<RecruitmentBoardInfo> getByIdWithLock(Long boardId);

    List<RecruitmentBoardInfo> getPublishedPageByNoOffset(int size, RecruitmentBoardInfo lastBoardInfo, RecruitmentBoardType boardType);

    Page<RecruitmentBoardInfo> getPublishedPageByPageNum(Long userId, Pageable pageable, RecruitmentBoardType recruitmentBoardType);

    Optional<RecruitmentBoardInfo> getByIdByWithQuestionList(Long boardId);

    RecruitmentBoardAndFormInfo patch(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo);

    void delete(RecruitmentBoardInfo recruitmentBoardInfo);

    void increaseCurrentMemberNum(Long recruitmentBoardId);

    void decreaseCurrentMemberNum(Long recruitmentBoardId);


    List<RecruitmentBoardInfo> getDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId);
}
