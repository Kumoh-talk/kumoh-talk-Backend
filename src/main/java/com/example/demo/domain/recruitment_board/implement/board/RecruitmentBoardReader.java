package com.example.demo.domain.recruitment_board.implement.board;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecruitmentBoardReader {
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    public Optional<RecruitmentBoardInfo> getById(Long boardId) {
        return recruitmentBoardRepository.getById(boardId);
    }

    public List<RecruitmentBoardInfo> getPublishedPageByNoOffset(int size, RecruitmentBoardInfo lastBoardInfo, RecruitmentBoardType boardType) {
        return recruitmentBoardRepository.getPublishedPageByNoOffset(size, lastBoardInfo, boardType);
    }

    public Page<RecruitmentBoardInfo> getPublishedPageByPageNum(Long userId, Pageable pageable, RecruitmentBoardType recruitmentBoardType) {
        return recruitmentBoardRepository.getPublishedPageByPageNum(userId, pageable, recruitmentBoardType);
    }

    public Optional<RecruitmentBoardInfo> getByIdByWithQuestionList(Long boardId) {
        return recruitmentBoardRepository.getByIdByWithQuestionList(boardId);
    }

    public Optional<RecruitmentBoardInfo> getLatestDraftIdByUserId(Long userId) {
        return Optional.of(recruitmentBoardRepository.getDraftPageByUserIdByNoOffset(userId, 1, null).get(0));
    }

    public List<RecruitmentBoardInfo> getDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId) {
        return recruitmentBoardRepository.getDraftPageByUserIdByNoOffset(userId, size, lastBoardId);
    }
}
