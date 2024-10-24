package com.example.demo.domain.recruitment_application.domain.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RecruitmentApplicantPageResponse {
    private int pageSize;
    private int pageNum;
    private int totalPage;
    private String pageSort;
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
    public static class RecruitmentApplicantInfoResponse {
        private Long applicantId;
        private Long userId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
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
