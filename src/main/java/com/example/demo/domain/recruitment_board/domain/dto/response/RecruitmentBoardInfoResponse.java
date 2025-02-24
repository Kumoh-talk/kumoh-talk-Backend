package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.application.board.dto.vo.Status;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentBoardInfoResponse", description = "모집 게시물 정보 응답")
public class RecruitmentBoardInfoResponse {
    @Schema(description = "모집 게시물 id 정보", example = "3")
    private Long boardId;
    @Schema(description = "모집 게시물 작성자 유저 id 정보", example = "1")
    private Long userId;
    @Schema(description = "모집 게시물 제목 정보", example = "board title")
    private String title;
    @Schema(description = "모집 게시물 요약 내용 정보", example = "board summary")
    private String summary;
    @Schema(description = "모집 게시물 주최자 정보", example = "board host")
    private String host;
    @Schema(description = "모집 게시물 내용 정보", example = "board content")
    private String content;
    @Schema(description = "모집 게시물 타입 정보[STUDY, PROJECT, MENTORING]", example = "STUDY")
    private RecruitmentBoardType type;
    @Schema(description = "모집 게시물 태그 정보[FRONTEND, BACKEND, AI, MOBILE, SECURITY, ETC]", example = "FRONTEND")
    private RecruitmentBoardTag tag;
    @Schema(description = "모집 게시물 상태 정보", example = "PUBLISHED")
    private Status status;
    @Schema(description = "모집 게시물 모집 대상 정보", example = "people using spring")
    private String recruitmentTarget;
    @Schema(description = "모집 게시물 모집 인원", example = "10")
    private Integer recruitmentNum;
    @Schema(description = "모집 게시물 현재 인원", example = "3")
    private Integer currentMemberNum;
    @Schema(description = "모집 시작 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime recruitmentStart;
    @Schema(description = "모집 마감 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime recruitmentDeadline;
    @Schema(description = "활동 시작 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activityStart;
    @Schema(description = "활동 종료 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activityFinish;
    @Schema(description = "활동 주기", example = "twice a week")
    private String activityCycle;

    public static RecruitmentBoardInfoResponse from(RecruitmentBoard recruitmentBoardEntity) {
        return RecruitmentBoardInfoResponse.builder()
                .boardId(recruitmentBoardEntity.getId())
                .userId(recruitmentBoardEntity.getUser().getId())
                .title(recruitmentBoardEntity.getTitle())
                .summary(recruitmentBoardEntity.getSummary())
                .host(recruitmentBoardEntity.getHost())
                .content(recruitmentBoardEntity.getContent())
                .type(recruitmentBoardEntity.getType())
                .tag(recruitmentBoardEntity.getTag())
                .status(recruitmentBoardEntity.getStatus())
                .recruitmentTarget(recruitmentBoardEntity.getRecruitmentTarget())
                .recruitmentNum(recruitmentBoardEntity.getRecruitmentNum())
                .currentMemberNum(recruitmentBoardEntity.getCurrentMemberNum())
                .recruitmentStart(recruitmentBoardEntity.getActivityStart())
                .recruitmentDeadline(recruitmentBoardEntity.getRecruitmentDeadline())
                .activityStart(recruitmentBoardEntity.getActivityStart())
                .activityFinish(recruitmentBoardEntity.getActivityFinish())
                .activityCycle(recruitmentBoardEntity.getActivityCycle())
                .build();
    }
}
