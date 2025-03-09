package com.example.demo.application.recruitment_application.dto.response;

import com.example.demo.domain.recruitment_application.entity.MyRecruitmentApplicationInfo;
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
@Schema(name = "MyRecruitmentApplicationInfoResponse", description = "사용자가 작성한 신청서 정보 응답")
public class MyRecruitmentApplicationInfoResponse {
    @Schema(description = "신청서 대상 모집 게시물 정보")
    private RecruitmentApplicationBoardResponse board;
    @Schema(description = "신청서 세부 정보")
    private RecruitmentApplicantInfoResponse application;

    public static MyRecruitmentApplicationInfoResponse from(MyRecruitmentApplicationInfo applicationInfo) {
        return MyRecruitmentApplicationInfoResponse.builder()
                .board(RecruitmentApplicationBoardResponse.from(applicationInfo))
                .application(RecruitmentApplicantInfoResponse.builder()
                        .applicantId(applicationInfo.getApplicationId())
                        .userId(applicationInfo.getUserId())
                        .createdAt(applicationInfo.getApplicationCreatedAt())
                        .updatedAt(applicationInfo.getApplicationUpdatedAt())
                        .build())
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "RecruitmentApplicationBoardResponse", description = "신청서 대상 모집 게시물 정보")
    public static class RecruitmentApplicationBoardResponse {
        @Schema(description = "모집 게시물 id 정보", example = "1")
        private Long id;
        @Schema(description = "모집 게시물 제목 정보", example = "board title")
        private String title;
        @Schema(description = "모집 게시물 타입 정보", example = "study")
        private RecruitmentBoardType type;
        @Schema(description = "모집 게시물 작성 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @Schema(description = "모집 게시물 수정 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;

        public static RecruitmentApplicationBoardResponse from(MyRecruitmentApplicationInfo applicationInfo) {
            return RecruitmentApplicationBoardResponse.builder()
                    .id(applicationInfo.getRecruitmentBoardId())
                    .title(applicationInfo.getBoardTitle())
                    .type(applicationInfo.getBoardType())
                    .createdAt(applicationInfo.getBoardCreatedAt())
                    .updatedAt(applicationInfo.getBoardUpdatedAt())
                    .build();
        }
    }
}

