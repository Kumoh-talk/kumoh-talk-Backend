package com.example.demo.application.recruitment_application.dto.response;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentApplicantInfoResponse", description = "신청서 세부 정보")
public class RecruitmentApplicantInfoResponse {
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

    public static RecruitmentApplicantInfoResponse from(RecruitmentApplicationInfo applicationInfo) {
        return RecruitmentApplicantInfoResponse.builder()
                .applicantId(applicationInfo.getApplicationId())
                .userId(applicationInfo.getUserId())
                .createdAt(applicationInfo.getCreatedAt())
                .updatedAt(applicationInfo.getUpdatedAt())
                .build();
    }
}