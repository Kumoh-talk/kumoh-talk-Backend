package com.example.demo.fixture.recruitment_board;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.domain.user.domain.User;

import java.time.LocalDateTime;


public class RecruitmentBoardFixtures {
    private static final String GENERAL_TITLE = "title";
    private static final String GENERAL_SUMMARY = "summary";
    private static final String GENERAL_HOST = "host";
    private static final String GENERAL_CONTENT = "content";

    private static final RecruitmentBoardType STUDY_RECRUITMENT_BOARD_TYPE = RecruitmentBoardType.STUDY;
    private static final RecruitmentBoardType PROJECT_RECRUITMENT_BOARD_TYPE = RecruitmentBoardType.PROJECT;
    private static final RecruitmentBoardType MENTORING_RECRUITMENT_BOARD_TYPE = RecruitmentBoardType.MENTORING;

    private static final RecruitmentBoardTag FRONTEND_RECRUITMENT_BOARD_TAG = RecruitmentBoardTag.FRONTEND;
    private static final RecruitmentBoardTag BACKEND_RECRUITMENT_BOARD_TAG = RecruitmentBoardTag.BACKEND;

    private static final Status PUBLISHED_STATUS = Status.PUBLISHED;
    private static final Status DRAFT_STATUS = Status.DRAFT;

    private static final String GENERAL_RECRUITMENT_TARGET = "target";
    private static final Integer GENERAL_RECRUITMENT_NUM = 3;
    private static final Integer GENERAL_CURRENT_MEMBER_NUM = 3;
    private static final LocalDateTime GENERAL_RECRUITMENT_DEADLINE = LocalDateTime.of(2025, 2, 28, 17, 0);
    private static final LocalDateTime GENERAL_ACTIVITY_START = LocalDateTime.of(2025, 2, 28, 17, 0);
    private static final LocalDateTime GENERAL_ACTIVITY_FINISH = LocalDateTime.of(2025, 2, 28, 17, 0);
    private static final String GENERAL_ACTIVITY_CYCLE = "cycle";

    public static RecruitmentBoard PUBLISHED_STUDY_RECRUITMENT_BOARD(User user) {
        return RecruitmentBoard.builder()
                .title(GENERAL_TITLE)
                .summary(GENERAL_SUMMARY)
                .host(GENERAL_HOST)
                .content(GENERAL_CONTENT)
                .type(STUDY_RECRUITMENT_BOARD_TYPE)
                .tag(FRONTEND_RECRUITMENT_BOARD_TAG)
                .status(PUBLISHED_STATUS)
                .recruitmentTarget(GENERAL_RECRUITMENT_TARGET)
                .recruitmentNum(GENERAL_RECRUITMENT_NUM)
                .currentMemberNum(GENERAL_CURRENT_MEMBER_NUM)
                .recruitmentDeadline(GENERAL_RECRUITMENT_DEADLINE)
                .activityStart(GENERAL_ACTIVITY_START)
                .activityFinish(GENERAL_ACTIVITY_FINISH)
                .activityCycle(GENERAL_ACTIVITY_CYCLE)
                .user(user)
                .build();
    }

    public static RecruitmentBoard DRAFT_STUDY_RECRUITMENT_BOARD(User user) {
        return RecruitmentBoard.builder()
                .title(GENERAL_TITLE)
                .summary(GENERAL_SUMMARY)
                .host(GENERAL_HOST)
                .content(GENERAL_CONTENT)
                .type(STUDY_RECRUITMENT_BOARD_TYPE)
                .tag(FRONTEND_RECRUITMENT_BOARD_TAG)
                .status(DRAFT_STATUS)
                .recruitmentTarget(GENERAL_RECRUITMENT_TARGET)
                .recruitmentNum(GENERAL_RECRUITMENT_NUM)
                .currentMemberNum(GENERAL_CURRENT_MEMBER_NUM)
                .recruitmentDeadline(GENERAL_RECRUITMENT_DEADLINE)
                .activityStart(GENERAL_ACTIVITY_START)
                .activityFinish(GENERAL_ACTIVITY_FINISH)
                .activityCycle(GENERAL_ACTIVITY_CYCLE)
                .user(user)
                .build();
    }
}
