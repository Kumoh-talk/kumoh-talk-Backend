package com.example.demo.application.recruitment_board.dto.response;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentBoardSummaryInfo", description = "모집 게시물 요약 정보 응답")
public class RecruitmentBoardSummaryResponse {
    @Schema(description = "모집 게시물 댓글 수", example = "3")
    private Long commentCount;
    @Schema(description = "모집 게시물 id 정보", example = "4")
    private Long boardId;
    @Schema(description = "모집 게시물 제목 정보", example = "board title")
    private String title;
    @Schema(description = "모집 게시물 요약 내용 정보", example = "board summary")
    private String summary;
    @Schema(description = "모집 게시물 타입 정보[STUDY, PROJECT, MENTORING]", example = "STUDY")
    private RecruitmentBoardType type;
    @Schema(description = "모집 게시물 태그 정보[FRONTEND, BACKEND, AI, MOBILE, SECURITY, ETC]", example = "FRONTEND")
    private RecruitmentBoardTag tag;
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

    public static RecruitmentBoardSummaryResponse from(RecruitmentBoardInfo recruitmentBoardInfo) {
        return RecruitmentBoardSummaryResponse.builder()
                .commentCount(recruitmentBoardInfo.getCommentCount() == null ? 0L : recruitmentBoardInfo.getCommentCount())
                .boardId(recruitmentBoardInfo.getBoardId())
                .title(recruitmentBoardInfo.getTitle())
                .summary(recruitmentBoardInfo.getSummary())
                .type(recruitmentBoardInfo.getType())
                .tag(recruitmentBoardInfo.getTag())
                .recruitmentTarget(recruitmentBoardInfo.getRecruitmentTarget())
                .recruitmentNum(recruitmentBoardInfo.getRecruitmentNum())
                .currentMemberNum(recruitmentBoardInfo.getCurrentMemberNum())
                .recruitmentStart(recruitmentBoardInfo.getCreatedAt())
                .recruitmentDeadline(recruitmentBoardInfo.getRecruitmentDeadline())
                .build();
    }
}

