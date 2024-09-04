package com.example.demo.domain.study_project_application.domain.response;

import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
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
public class MyStudyProjectApplicationPageResponse {
    private int pageSize;
    private int pageNum;
    private int totalPage;
    private String pageSort;
    private List<MyStudyProjectApplicationInfoResponse> applicationList;

    public static MyStudyProjectApplicationPageResponse from(Page<StudyProjectApplicant> applicantPageEntity) {
        return MyStudyProjectApplicationPageResponse.builder()
                .pageSize(applicantPageEntity.getSize())
                .pageNum(applicantPageEntity.getNumber() + 1)
                .totalPage(applicantPageEntity.getTotalPages())
                .pageSort(applicantPageEntity.getSort().toString())
                .applicationList(applicantPageEntity.getContent().stream()
                        .map(MyStudyProjectApplicationInfoResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MyStudyProjectApplicationInfoResponse {
        private StudyProjectApplicationBoardResponse board;
        private StudyProjectApplicantPageResponse.StudyProjectApplicantInfoResponse application;

        public static MyStudyProjectApplicationInfoResponse from(StudyProjectApplicant studyProjectApplicant) {
            return MyStudyProjectApplicationInfoResponse.builder()
                    .board(StudyProjectApplicationBoardResponse.from(studyProjectApplicant))
                    .application(StudyProjectApplicantPageResponse.StudyProjectApplicantInfoResponse.from(studyProjectApplicant))
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StudyProjectApplicationBoardResponse {
        private Long id;
        private String title;
        private StudyProjectBoardType type;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime updatedAt;

        public static StudyProjectApplicationBoardResponse from(StudyProjectApplicant studyProjectApplicant) {
            return StudyProjectApplicationBoardResponse.builder()
                    .id(studyProjectApplicant.getStudyProjectBoard().getId())
                    .title(studyProjectApplicant.getStudyProjectBoard().getTitle())
                    .type(studyProjectApplicant.getStudyProjectBoard().getType())
                    .createdAt(studyProjectApplicant.getStudyProjectBoard().getCreatedAt())
                    .updatedAt(studyProjectApplicant.getStudyProjectBoard().getUpdatedAt())
                    .build();
        }
    }
}
