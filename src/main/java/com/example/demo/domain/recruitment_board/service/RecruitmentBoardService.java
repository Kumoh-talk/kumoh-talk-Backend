package com.example.demo.domain.recruitment_board.service;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicantRepository;
import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentBoardInfoAndFormRequest;
import com.example.demo.domain.recruitment_board.domain.dto.response.*;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormChoiceAnswer;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormChoiceAnswerRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormQuestionRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.vo.Role;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentBoardService {
    private final UserService userService;
    private final UserAdditionalInfoService userAdditionalInfoService;

    private final RecruitmentBoardRepository recruitmentBoardRepository;
    private final RecruitmentFormQuestionRepository recruitmentFormQuestionRepository;
    private final RecruitmentFormChoiceAnswerRepository recruitmentFormChoiceAnswerRepository;
    private final RecruitmentApplicantRepository recruitmentApplicantRepository;

    @Transactional
    public RecruitmentBoardInfoAndFormResponse saveBoardAndForm(
            Long userId,
            RecruitmentBoardInfoAndFormRequest recruitmentBoardInfoAndFormRequest,
            Status status) {
        User user = userService.validateUser(userId);

        RecruitmentBoard recruitmentBoard = RecruitmentBoard.from(recruitmentBoardInfoAndFormRequest, user, status);
        RecruitmentBoard savedBoard = recruitmentBoardRepository.save(recruitmentBoard);

        return RecruitmentBoardInfoAndFormResponse.from(savedBoard);
    }

    @Transactional(readOnly = true)
    public RecruitmentBoardNoOffsetResponse getPublishedBoardListByNoOffset(int size, Long lastBoardId, RecruitmentBoardType boardType) {
        List<RecruitmentBoard> recruitmentBoardList;

        // 최근 게시물 Id를 알 수 없을 때(첫 페이지를 조회할 때) -> 쿼리를 통해 첫 게시물 id를 가져온다.
        if (lastBoardId == null) {
            List<Long> boardIdList;
            Long firstId;
            try {
                boardIdList = recruitmentBoardRepository.findPublishedId(boardType)
                        .orElseThrow(() -> new NullPointerException("can't find any Board"));
                firstId = boardIdList.get(0);
            } catch (NullPointerException e) {
                return RecruitmentBoardNoOffsetResponse.newEmptyListInstance(size);
            }

            RecruitmentBoard firstBoard = validateRecruitmentBoard(firstId);
            // firstId 이하 게시물을 찾기
            recruitmentBoardList = recruitmentBoardRepository.findPublishedPageByNoOffset(size, firstBoard, boardType, true);
        } else {
            RecruitmentBoard lastBoard = validateRecruitmentBoard(lastBoardId);
            // lastBoardId 미만 게시물을 찾기
            recruitmentBoardList = recruitmentBoardRepository.findPublishedPageByNoOffset(size, lastBoard, boardType, false);
        }

        return RecruitmentBoardNoOffsetResponse.from(size, recruitmentBoardList);
    }

    @Transactional(readOnly = true)
    public RecruitmentBoardPageNumResponse getPublishedBoardListByPageNum(Pageable pageable, RecruitmentBoardType boardType) {
        Page<RecruitmentBoard> recruitmentBoardList = recruitmentBoardRepository.findPublishedPageByPageNum(pageable, boardType);

        return RecruitmentBoardPageNumResponse.from(recruitmentBoardList);
    }

    @Transactional(readOnly = true)
    public RecruitmentBoardNoOffsetResponse getDraftBoardListByUserId(Long userId, int size, Long lastBoardId) {
        userService.validateUser(userId);

        List<RecruitmentBoard> recruitmentBoardList;
        if (lastBoardId == null) {
            Long firstId;
            try {
                firstId = recruitmentBoardRepository.findFirstDraftIdByUserId(userId)
                        .orElseThrow(() -> new NullPointerException("게시물이 존재하지 않습니다."));
            } catch (NullPointerException e) {
                return RecruitmentBoardNoOffsetResponse.newEmptyListInstance(size);
            }
            recruitmentBoardList = recruitmentBoardRepository.findDraftPageByUserIdByNoOffset(userId, size, firstId, true);
        } else {
            recruitmentBoardList = recruitmentBoardRepository.findDraftPageByUserIdByNoOffset(userId, size, lastBoardId, false);
        }

        return RecruitmentBoardNoOffsetResponse.from(size, recruitmentBoardList);
    }

    @Transactional(readOnly = true)
    public RecruitmentBoardPageNumResponse getPublishedBoardListByUserId(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        userService.validateUser(userId);
        Page<RecruitmentBoard> recruitmentBoardList = recruitmentBoardRepository.findPublishedPageByUserIdByPageNum(userId, pageable, boardType);

        return RecruitmentBoardPageNumResponse.from(recruitmentBoardList);
    }

    @Transactional(readOnly = true)
    public RecruitmentBoardInfoResponse getBoardInfo(Long recruitmentBoardId) {
        RecruitmentBoard recruitmentBoard = validateRecruitmentBoard(recruitmentBoardId);

        return RecruitmentBoardInfoResponse.from(recruitmentBoard);
    }

    @Transactional(readOnly = true)
    public List<RecruitmentFormQuestionResponse> getFormInfoList(Long recruitmentBoardId) {
        List<RecruitmentFormQuestion> recruitmentFormQuestionList = recruitmentFormQuestionRepository.findByBoard_IdByFetchingAnswerList(recruitmentBoardId)
                .orElse(new ArrayList<>());

        if (recruitmentFormQuestionList.isEmpty())
            return new ArrayList<>();
        else {
            return recruitmentFormQuestionList.stream()
                    .map(RecruitmentFormQuestionResponse::from)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public RecruitmentBoardInfoAndFormResponse updateBoardAndForm(
            Long userId,
            Long recruitmentBoardId,
            RecruitmentBoardInfoAndFormRequest recruitmentBoardInfoAndFormRequest,
            Status status) {
        RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.findByIdByFetchingChoiceAnswerList(recruitmentBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        if (!userId.equals(recruitmentBoard.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        // 신청자가 존재하면 수정 불가
        if (recruitmentApplicantRepository.existsByRecruitmentBoard_Id(recruitmentBoardId)) {
            throw new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_EXIST);
        }

        // 게시물 업데이트
        recruitmentBoard.updateFromRequest(recruitmentBoardInfoAndFormRequest, status,
                recruitmentFormQuestionRepository, recruitmentFormChoiceAnswerRepository);

        // TODO : Request enum valid 문제
        return RecruitmentBoardInfoAndFormResponse.from(recruitmentBoard);
    }

    @Transactional
    public void deleteBoardAndForm(
            Long userId,
            Long recruitmentBoardId) {
        RecruitmentBoard recruitmentBoard = validateRecruitmentBoard(recruitmentBoardId);
        Role userRole = userService.validateUser(userId).getRole();

        if (!userId.equals(recruitmentBoard.getUser().getId()) && userRole != Role.ROLE_ADMIN) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        // soft delete
        List<RecruitmentFormQuestion> questionList = recruitmentFormQuestionRepository.findByBoard_IdByFetchingAnswerList(recruitmentBoardId)
                .orElse(new ArrayList<>());
        for (RecruitmentFormQuestion recruitmentFormQuestion : questionList) {
            List<Long> questionIds = new ArrayList<>();
            List<Long> answerIds = new ArrayList<>();

            questionIds.add(recruitmentFormQuestion.getId());
            for (RecruitmentFormChoiceAnswer recruitmentFormChoiceAnswer : recruitmentFormQuestion.getRecruitmentFormChoiceAnswerList()) {
                answerIds.add(recruitmentFormChoiceAnswer.getId());
            }
            recruitmentFormChoiceAnswerRepository.softDeleteAnswersByIds(answerIds);
            recruitmentFormQuestionRepository.softDeleteQuestionsByIds(questionIds);
        }
        recruitmentBoardRepository.delete(recruitmentBoard);
    }

    @Transactional(readOnly = true)
    public RecruitmentBoardInfoAndFormResponse getLatestDraftBoardAndForm(Long userId) {
        userService.validateUser(userId);

        Long recruitmentBoardId = recruitmentBoardRepository.findFirstDraftIdByUserId(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        return RecruitmentBoardInfoAndFormResponse
                .builder()
                .board(getBoardInfo(recruitmentBoardId))
                .form(getFormInfoList(recruitmentBoardId))
                .build();
    }

    public RecruitmentBoard validateRecruitmentBoard(Long recruitmentBoardId) {
        return recruitmentBoardRepository.findById(recruitmentBoardId).orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }
}
