package com.example.demo.domain.study_project_application.domain.response;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
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
public class StudyProjectApplicantPageResponse {
    private int pageSize;
    private int pageNum;
    private int totalPage;
    private String pageSort;
    private List<StudyProjectApplicantInfoResponse> applicantList;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StudyProjectApplicantInfoResponse {
        private Long applicantId;
        private Long userId;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;

        public static StudyProjectApplicantInfoResponse from(StudyProjectApplicant applicant) {
            return StudyProjectApplicantInfoResponse.builder()
                    .applicantId(applicant.getId())
                    .userId(applicant.getUser().getId())
                    .createdAt(applicant.getCreatedAt())
                    .updatedAt(applicant.getUpdatedAt())
                    .build();
        }
    }

    public static StudyProjectApplicantPageResponse from(Page<StudyProjectApplicant> studyProjectApplicantList) {
        return StudyProjectApplicantPageResponse.builder()
                .pageSize(studyProjectApplicantList.getSize())
                .pageNum(studyProjectApplicantList.getNumber() + 1)
                .totalPage(studyProjectApplicantList.getTotalPages())
                .pageSort(studyProjectApplicantList.getSort().toString())
                .applicantList(studyProjectApplicantList.getContent().stream()
                        .map(StudyProjectApplicantInfoResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
