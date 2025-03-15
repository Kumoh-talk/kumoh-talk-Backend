package com.example.demo.infra.recruitment_board.repository.impl;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardAndFormInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.base.EntityFinder;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentBoardJpaRepository;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormAnswerJpaRepository;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormQuestionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecruitmentBoardRepositoryImpl implements RecruitmentBoardRepository {
    private final RecruitmentBoardJpaRepository recruitmentBoardJpaRepository;
    private final RecruitmentFormQuestionJpaRepository recruitmentFormQuestionJpaRepository;
    private final RecruitmentFormAnswerJpaRepository recruitmentFormAnswerJpaRepository;
    private final EntityFinder entityFinder;

    @Override
    public RecruitmentBoardAndFormInfo save(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo) {
        User user = entityFinder.findUserById(recruitmentBoardAndFormInfo.getBoard().getUserId());

        RecruitmentBoard recruitmentBoard = RecruitmentBoard.from(recruitmentBoardAndFormInfo, user);
        RecruitmentBoard savedBoard = recruitmentBoardJpaRepository.save(recruitmentBoard);
        return savedBoard.toBoardAndFormInfoDomain();
    }

    @Override
    public Optional<RecruitmentBoardInfo> getById(Long boardId) {
        return recruitmentBoardJpaRepository.findById(boardId)
                .map(RecruitmentBoard::toBoardInfoDomain);
    }

    @Override
    public Optional<RecruitmentBoardInfo> getByIdWithUser(Long boardId) {
        return recruitmentBoardJpaRepository.findByIdWithUser(boardId)
                .map(RecruitmentBoard.class::cast)
                .map(RecruitmentBoard::toBoardInfoDomain);
    }

    @Override
    public List<RecruitmentBoardInfo> getPublishedPageByNoOffset(int size, RecruitmentBoardInfo lastBoardInfo, RecruitmentBoardType boardType) {
        return recruitmentBoardJpaRepository.findPublishedPageByNoOffset(size, lastBoardInfo, boardType);
    }

    @Override
    public Page<RecruitmentBoardInfo> getPublishedPageByPageNum(Long userId, Pageable pageable, RecruitmentBoardType recruitmentBoardType) {
        return recruitmentBoardJpaRepository.findPublishedPageByPageNum(userId, pageable, recruitmentBoardType);
    }

    @Override
    public Optional<RecruitmentBoardInfo> getByIdByWithQuestionList(Long boardId) {
        return recruitmentBoardJpaRepository.findByIdByFetchingQuestionList(boardId)
                .map(RecruitmentBoard::toBoardInfoDomain);
    }

    @Override
    public RecruitmentBoardAndFormInfo patch(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo) {
        RecruitmentBoard originRecruitmentBoard = findRecruitmentBoardById(recruitmentBoardAndFormInfo.getBoard().getBoardId());
        originRecruitmentBoard.update(recruitmentBoardAndFormInfo, recruitmentFormQuestionJpaRepository, recruitmentFormAnswerJpaRepository);
        return originRecruitmentBoard.toBoardAndFormInfoDomain();
    }

    @Override
    public void delete(RecruitmentBoardInfo recruitmentBoardInfo) {
        RecruitmentBoard recruitmentBoard = findRecruitmentBoardById(recruitmentBoardInfo.getBoardId());
        recruitmentBoardJpaRepository.delete(recruitmentBoard);
    }

    @Override
    public List<RecruitmentBoardInfo> getDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId) {
        return recruitmentBoardJpaRepository.findDraftPageByUserIdByNoOffset(userId, size, lastBoardId)
                .stream().map(RecruitmentBoard::toBoardInfoDomain).collect(Collectors.toList());
    }

    public RecruitmentBoard findRecruitmentBoardById(Long boardId) {
        RecruitmentBoard recruitmentBoard = entityFinder.findById(RecruitmentBoard.class, boardId);
        return recruitmentBoard != null ? recruitmentBoard : recruitmentBoardJpaRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }
}
