package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentBoardNoOffsetResponse", description = "no-offset 방식 모집 게시물 페이지 응답")
public class RecruitmentBoardNoOffsetResponse {
    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean nextPage;
    @Schema(description = "한 페이지에 게시물 갯수", example = "10")
    private int pageSize;
    @Schema(description = "모집 게시물 요약 정보 리스트")
    private List<RecruitmentBoardSummaryInfo> boardInfo;

    public static RecruitmentBoardNoOffsetResponse newEmptyListInstance(int pageSize) {
        return RecruitmentBoardNoOffsetResponse.builder()
                .nextPage(false)
                .pageSize(pageSize)
                .boardInfo(new ArrayList<>())
                .build();
    }

    public static RecruitmentBoardNoOffsetResponse from(int pageSize, List<RecruitmentBoard> recruitmentBoardList) {
        boolean nextPage = false;
        if (recruitmentBoardList.size() > pageSize) {
            nextPage = true;
            recruitmentBoardList.remove(pageSize);
        }
        return RecruitmentBoardNoOffsetResponse.builder()
                .nextPage(nextPage)
                .pageSize(pageSize)
                .boardInfo(recruitmentBoardList.stream()
                        .map(RecruitmentBoardSummaryInfo::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "RecruitmentBoardSummaryInfo", description = "모집 게시물 요약 정보 응답")
    public static class RecruitmentBoardSummaryInfo {
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

        public static RecruitmentBoardSummaryInfo from(RecruitmentBoard recruitmentBoardEntity) {
            return RecruitmentBoardSummaryInfo.builder()
                    .boardId(recruitmentBoardEntity.getId())
                    .title(recruitmentBoardEntity.getTitle())
                    .summary(recruitmentBoardEntity.getSummary())
                    .type(recruitmentBoardEntity.getType())
                    .tag(recruitmentBoardEntity.getTag())
                    .recruitmentTarget(recruitmentBoardEntity.getRecruitmentTarget())
                    .recruitmentNum(recruitmentBoardEntity.getRecruitmentNum())
                    .currentMemberNum(recruitmentBoardEntity.getCurrentMemberNum())
                    .recruitmentStart(recruitmentBoardEntity.getActivityStart())
                    .recruitmentDeadline(recruitmentBoardEntity.getRecruitmentDeadline())
                    .build();
        }
    }
}

