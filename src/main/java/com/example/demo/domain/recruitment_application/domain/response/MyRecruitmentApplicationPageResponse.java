package com.example.demo.domain.recruitment_application.domain.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
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
public class MyRecruitmentApplicationPageResponse {
    private int pageSize;
    private int pageNum;
    private int totalPage;
    private String pageSort;
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
    public static class MyRecruitmentApplicationInfoResponse {
        private RecruitmentApplicationBoardResponse board;
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
    public static class RecruitmentApplicationBoardResponse {
        private Long id;
        private String title;
        private RecruitmentBoardType type;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
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
