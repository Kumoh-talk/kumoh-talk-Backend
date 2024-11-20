package com.example.demo.domain.recruitment_application.domain.dto.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
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
@Schema(name = "RecruitmentApplicantPageResponse", description = "모집 게시물 신청자 페이지 정보 응답")
public class RecruitmentApplicantPageResponse {
    @Schema(description = "한 페이지에 신청서 갯수", example = "10")
    private int pageSize;
    @Schema(description = "현재 페이지 number", example = "1")
    private int pageNum;
    @Schema(description = "총 페이지 갯수", example = "10")
    private int totalPage;
    @Schema(description = "정렬 기준 및 방향", example = "createdAt: DESC")
    private String pageSort;
    @Schema(description = "신청서 세부 정보 리스트")
    private List<RecruitmentApplicantInfoResponse> applicantList;

    public static RecruitmentApplicantPageResponse from(Page<RecruitmentApplicant> recruitmentApplicantList) {
        return RecruitmentApplicantPageResponse.builder()
                .pageSize(recruitmentApplicantList.getSize())
                .pageNum(recruitmentApplicantList.getNumber() + 1)
                .totalPage(recruitmentApplicantList.getTotalPages())
                .pageSort(recruitmentApplicantList.getSort().toString())
                .applicantList(recruitmentApplicantList.getContent().stream()
                        .map(RecruitmentApplicantInfoResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "RecruitmentApplicantInfoResponse", description = "신청서 세부 정보")
    public static class RecruitmentApplicantInfoResponse {
        @Schema(description = "신청인 id 정보", example = "1")
        private Long applicantId;
        @Schema(description = "신청인 유저 id 정보", example = "3")
        private Long userId;
        @Schema(description = "신청서 작성 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @Schema(description = "신청서 수정 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;

        public static RecruitmentApplicantInfoResponse from(RecruitmentApplicant applicant) {
            return RecruitmentApplicantInfoResponse.builder()
                    .applicantId(applicant.getId())
                    .userId(applicant.getUser().getId())
                    .createdAt(applicant.getCreatedAt())
                    .updatedAt(applicant.getUpdatedAt())
                    .build();
        }
    }
}
