package com.example.demo.application.recruitment_application.dto.response;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentApplicationResponse", description = "모집 게시물 신청서 응답")
public class RecruitmentApplicationResponse {
    @Schema(description = "신청자 id 정보", example = "1")
    private final Long applicantId;
    @Schema(description = "신청자의 신청서 정보 리스트")
    private final List<RecruitmentApplicationAnswerInfoResponse> applicantAnswer;

    public static RecruitmentApplicationResponse from(RecruitmentApplicationInfo recruitmentApplicationInfo) {
        return RecruitmentApplicationResponse.builder()
                .applicantId(recruitmentApplicationInfo.getApplicationId())
                .applicantAnswer(recruitmentApplicationInfo.getAnswerInfoList().stream()
                        .map(RecruitmentApplicationAnswerInfoResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
