package com.example.demo.domain.recruitment_board.entity;

import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecruitmentBoardInfo {
    private Long commentCount;
    private Long boardId;
    private Long userId;
    private String userNickname;
    private String title;
    private String summary;
    private String host;
    private String content;
    private RecruitmentBoardType type;
    private RecruitmentBoardTag tag;
    private Status status;
    private String recruitmentTarget;
    private Integer recruitmentNum;
    private Integer currentMemberNum;
    private LocalDateTime recruitmentDeadline;
    private LocalDateTime activityStart;
    private LocalDateTime activityFinish;
    private String activityCycle;
    private LocalDateTime createdAt;

    @Builder
    public RecruitmentBoardInfo(Long commentCount, Long boardId, Long userId, String userNickname, String title, String summary, String host, String content, RecruitmentBoardType type, RecruitmentBoardTag tag, Status status, String recruitmentTarget, Integer recruitmentNum, Integer currentMemberNum, LocalDateTime recruitmentDeadline, LocalDateTime activityStart, LocalDateTime activityFinish, String activityCycle, LocalDateTime createdAt) {
        this.commentCount = commentCount;
        this.boardId = boardId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.title = title;
        this.summary = summary;
        this.host = host;
        this.content = content;
        this.type = type;
        this.tag = tag;
        this.status = status;
        this.recruitmentTarget = recruitmentTarget;
        this.recruitmentNum = recruitmentNum;
        this.currentMemberNum = currentMemberNum;
        this.recruitmentDeadline = recruitmentDeadline;
        this.activityStart = activityStart;
        this.activityFinish = activityFinish;
        this.activityCycle = activityCycle;
        this.createdAt = createdAt;
    }
}
