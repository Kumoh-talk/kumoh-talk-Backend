package com.example.demo.domain.study_project_board.service;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.study_project_application.repository.StudyProjectApplicantRepository;
import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectBoardInfoAndFormRequest;
import com.example.demo.domain.study_project_board.domain.dto.response.*;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormChoiceAnswer;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectFormQuestion;
import com.example.demo.domain.study_project_board.repository.StudyProjectBoardRepository;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormChoiceAnswerRepository;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormQuestionRepository;
import com.example.demo.domain.user.domain.User;
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
public class StudyProjectBoardService {
    private final UserService userService;
    private final UserAdditionalInfoService userAdditionalInfoService;

    private final StudyProjectBoardRepository studyProjectBoardRepository;
    private final StudyProjectFormQuestionRepository studyProjectFormQuestionRepository;
    private final StudyProjectFormChoiceAnswerRepository studyProjectFormChoiceAnswerRepository;
    private final StudyProjectApplicantRepository studyProjectApplicantRepository;

    @Transactional
    public StudyProjectBoardInfoAndFormResponse saveBoardAndForm(
            Long userId,
            StudyProjectBoardInfoAndFormRequest studyProjectBoardInfoAndFormRequest,
            Status status) {
        User user = userService.validateUser(userId);
        userAdditionalInfoService.validateUserAdditionalInfo(user.getUserAdditionalInfo());

        StudyProjectBoard studyProjectBoard = StudyProjectBoard.from(studyProjectBoardInfoAndFormRequest, user, status);
        StudyProjectBoard savedBoard = studyProjectBoardRepository.save(studyProjectBoard);

        return StudyProjectBoardInfoAndFormResponse.from(savedBoard);
    }

    @Transactional(readOnly = true)
    public StudyProjectBoardNoOffsetResponse getPublishedBoardListByNoOffset(int size, Long lastBoardId, StudyProjectBoardType boardType) {
        List<StudyProjectBoard> studyProjectBoardList;

        // 최근 게시물 Id를 알 수 없을 때(첫 페이지를 조회할 때) -> 쿼리를 통해 첫 게시물 id를 가져온다.
        if (lastBoardId == null) {
            List<Long> boardIdList;
            Long firstId;
            try {
                boardIdList = studyProjectBoardRepository.findPublishedId(boardType)
                        .orElseThrow(() -> new NullPointerException("can't find any Board"));
                firstId = boardIdList.get(0);
            } catch (NullPointerException e) {
                return StudyProjectBoardNoOffsetResponse.newEmptyListInstance(size);
            }

            StudyProjectBoard firstBoard = validateStudyProjectBoard(firstId);
            // firstId 이하 게시물을 찾기
            studyProjectBoardList = studyProjectBoardRepository.findPublishedPageByNoOffset(size, firstBoard, boardType, true);
        } else {
            StudyProjectBoard lastBoard = validateStudyProjectBoard(lastBoardId);
            // lastBoardId 미만 게시물을 찾기
            studyProjectBoardList = studyProjectBoardRepository.findPublishedPageByNoOffset(size, lastBoard, boardType, false);
        }

        return StudyProjectBoardNoOffsetResponse.from(size, studyProjectBoardList);
    }

    @Transactional(readOnly = true)
    public StudyProjectBoardPageNumResponse getPublishedBoardListByPageNum(Pageable pageable, StudyProjectBoardType boardType) {
        Page<StudyProjectBoard> studyProjectBoardList = studyProjectBoardRepository.findPublishedPageByPageNum(pageable, boardType);

        return StudyProjectBoardPageNumResponse.from(studyProjectBoardList);
    }

    @Transactional(readOnly = true)
    public StudyProjectBoardNoOffsetResponse getDraftBoardListByUserId(Long userId, int size, Long lastBoardId) {
        userService.validateUser(userId);

        List<StudyProjectBoard> studyProjectBoardList;
        if (lastBoardId == null) {
            Long firstId;
            try {
                firstId = studyProjectBoardRepository.findFirstDraftIdByUserId(userId)
                        .orElseThrow(() -> new NullPointerException("게시물이 존재하지 않습니다."));
            } catch (NullPointerException e) {
                return StudyProjectBoardNoOffsetResponse.newEmptyListInstance(size);
            }
            studyProjectBoardList = studyProjectBoardRepository.findDraftPageByUserIdByNoOffset(userId, size, firstId, true);
        } else {
            studyProjectBoardList = studyProjectBoardRepository.findDraftPageByUserIdByNoOffset(userId, size, lastBoardId, false);
        }

        return StudyProjectBoardNoOffsetResponse.from(size, studyProjectBoardList);
    }

    @Transactional(readOnly = true)
    public StudyProjectBoardPageNumResponse getPublishedBoardListByUserId(Long userId, Pageable pageable, StudyProjectBoardType boardType) {
        userService.validateUser(userId);
        Page<StudyProjectBoard> studyProjectBoardList = studyProjectBoardRepository.findPublishedPageByUserIdByPageNum(userId, pageable, boardType);

        return StudyProjectBoardPageNumResponse.from(studyProjectBoardList);
    }

    @Transactional(readOnly = true)
    public StudyProjectBoardInfoResponse getBoardInfo(Long studyProjectBoardId) {
        StudyProjectBoard studyProjectBoard = validateStudyProjectBoard(studyProjectBoardId);

        return StudyProjectBoardInfoResponse.from(studyProjectBoard);
    }

    @Transactional(readOnly = true)
    public List<StudyProjectFormQuestionResponse> getFormInfoList(Long studyProjectBoardId) {
        List<StudyProjectFormQuestion> studyProjectFormQuestionList = studyProjectFormQuestionRepository.findByBoard_IdByFetchingAnswerList(studyProjectBoardId)
                .orElse(new ArrayList<>());

        if (studyProjectFormQuestionList.isEmpty())
            return new ArrayList<>();
        else {
            return studyProjectFormQuestionList.stream()
                    .map(StudyProjectFormQuestionResponse::from)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public StudyProjectBoardInfoAndFormResponse updateBoardAndForm(
            Long userId,
            Long studyProjectBoardId,
            StudyProjectBoardInfoAndFormRequest studyProjectBoardInfoAndFormRequest,
            Status status) {
        StudyProjectBoard studyProjectBoard = studyProjectBoardRepository.findByIdByFetchingChoiceAnswerList(studyProjectBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        if (!userId.equals(studyProjectBoard.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        // 신청자가 존재하면 수정 불가
        if (studyProjectApplicantRepository.existsByStudyProjectBoard_Id(studyProjectBoardId)) {
            throw new ServiceException(ErrorCode.STUDYPROJECT_APPLICATION_EXIST);
        }

        // 게시물 업데이트
        studyProjectBoard.updateFromRequest(studyProjectBoardInfoAndFormRequest, status,
                studyProjectFormQuestionRepository, studyProjectFormChoiceAnswerRepository);

        // TODO : Request enum valid 문제
        return StudyProjectBoardInfoAndFormResponse.from(studyProjectBoard);
    }

    @Transactional
    public void deleteBoardAndForm(
            Long userId,
            Long studyProjectBoardId,
            String userRole) {
        StudyProjectBoard studyProjectBoard = validateStudyProjectBoard(studyProjectBoardId);

        if (!userId.equals(studyProjectBoard.getUser().getId()) && !userRole.equals("ROLE_ADMIN")) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }

        // soft delete
        List<StudyProjectFormQuestion> questionList = studyProjectFormQuestionRepository.findByBoard_IdByFetchingAnswerList(studyProjectBoardId)
                .orElse(new ArrayList<>());
        for (StudyProjectFormQuestion studyProjectFormQuestion : questionList) {
            List<Long> questionIds = new ArrayList<>();
            List<Long> answerIds = new ArrayList<>();

            questionIds.add(studyProjectFormQuestion.getId());
            for (StudyProjectFormChoiceAnswer studyProjectFormChoiceAnswer : studyProjectFormQuestion.getStudyProjectFormChoiceAnswerList()) {
                answerIds.add(studyProjectFormChoiceAnswer.getId());
            }
            studyProjectFormChoiceAnswerRepository.softDeleteAnswersByIds(answerIds);
            studyProjectFormQuestionRepository.softDeleteQuestionsByIds(questionIds);
        }
        studyProjectBoardRepository.delete(studyProjectBoard);
    }

    @Transactional(readOnly = true)
    public StudyProjectBoardInfoAndFormResponse getLatestDraftBoardAndForm(Long userId) {
        userService.validateUser(userId);

        Long studyProjectBoardId = studyProjectBoardRepository.findFirstDraftIdByUserId(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        return StudyProjectBoardInfoAndFormResponse
                .builder()
                .board(getBoardInfo(studyProjectBoardId))
                .form(getFormInfoList(studyProjectBoardId))
                .build();
    }

    public StudyProjectBoard validateStudyProjectBoard(Long studyProjectBoardId) {
        return studyProjectBoardRepository.findById(studyProjectBoardId).orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
    }
}
