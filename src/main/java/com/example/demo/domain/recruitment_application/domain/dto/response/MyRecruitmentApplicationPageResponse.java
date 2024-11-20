package com.example.demo.domain.recruitment_application.domain.dto.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "MyRecruitmentApplicationPageResponse", description = "사용자 작성 신청서 페이지 정보 응답")
public class MyRecruitmentApplicationPageResponse {
    @Schema(description = "한 페이지에 신청서 갯수", example = "10")
    private int pageSize;
    @Schema(description = "현재 페이지 number", example = "1")
    private int pageNum;
    @Schema(description = "총 페이지 갯수", example = "10")
    private int totalPage;
    @Schema(description = "정렬 기준 및 방향", example = "createdAt: DESC")
    private String pageSort;
    @Schema(description = "사용자가 작성한 신청서 정보 리스트")
    private List<MyRecruitmentApplicationInfoResponse> applicationList;

    public static MyRecruitmentApplicationPageResponse from(Page<RecruitmentApplicant> applicantPageEntity) {
        return MyRecruitmentApplicationPageResponse.builder()
                .pageSize(applicantPageEntity.getSize())
                .pageNum(applicantPageEntity.getNumber() + 1)
                .totalPage(applicantPageEntity.getTotalPages())
                .pageSort(applicantPageEntity.getSort().toString())
                .applicationList(applicantPageEntity.getContent().stream()
                        .map(MyRecruitmentApplicationInfoResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "MyRecruitmentApplicationInfoResponse", description = "사용자가 작성한 신청서 정보 응답")
    public static class MyRecruitmentApplicationInfoResponse {
        @Schema(description = "신청서 대상 모집 게시물 정보")
        private RecruitmentApplicationBoardResponse board;
        @Schema(description = "신청서 세부 정보")
        private RecruitmentApplicantPageResponse.RecruitmentApplicantInfoResponse application;

        public static MyRecruitmentApplicationInfoResponse from(RecruitmentApplicant recruitmentApplicant) {
            return MyRecruitmentApplicationInfoResponse.builder()
                    .board(RecruitmentApplicationBoardResponse.from(recruitmentApplicant))
                    .application(RecruitmentApplicantPageResponse.RecruitmentApplicantInfoResponse.from(recruitmentApplicant))
                    .build();
        }
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

        public static RecruitmentApplicationBoardResponse from(RecruitmentApplicant recruitmentApplicant) {
            return RecruitmentApplicationBoardResponse.builder()
                    .id(recruitmentApplicant.getRecruitmentBoard().getId())
                    .title(recruitmentApplicant.getRecruitmentBoard().getTitle())
                    .type(recruitmentApplicant.getRecruitmentBoard().getType())
                    .createdAt(recruitmentApplicant.getRecruitmentBoard().getCreatedAt())
                    .updatedAt(recruitmentApplicant.getRecruitmentBoard().getUpdatedAt())
                    .build();
        }
    }
}
