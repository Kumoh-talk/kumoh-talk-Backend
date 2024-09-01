package com.example.demo.domain.study_project_board.domain.dto.response;

import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardTag;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class StudyProjectBoardNoOffsetResponse {
    private boolean nextPage;
    private int pageSize;
    private List<StudyProjectBoardSummaryInfo> boardInfo;

    public static StudyProjectBoardNoOffsetResponse newEmptyListInstance(int pageSize) {
        return StudyProjectBoardNoOffsetResponse.builder()
                .nextPage(false)
                .pageSize(pageSize)
                .boardInfo(new ArrayList<>())
                .build();
    }

    public static StudyProjectBoardNoOffsetResponse from(int pageSize, List<StudyProjectBoard> studyProjectBoardList) {
        boolean nextPage = false;
        if (studyProjectBoardList.size() > pageSize) {
            nextPage = true;
            studyProjectBoardList.remove(pageSize);
        }
        return StudyProjectBoardNoOffsetResponse.builder()
                .nextPage(nextPage)
                .pageSize(pageSize)
                .boardInfo(studyProjectBoardList.stream()
                        .map(StudyProjectBoardSummaryInfo::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class StudyProjectBoardSummaryInfo {

        private Long boardId;

        private String title;

        private String summary;

        private StudyProjectBoardType type;

        private StudyProjectBoardTag tag;

        private String recruitmentTarget;

        private String recruitmentNum;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime recruitmentStart;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime recruitmentDeadline;

        public static StudyProjectBoardSummaryInfo from(StudyProjectBoard studyProjectBoardEntity) {
            return StudyProjectBoardSummaryInfo.builder()
                    .boardId(studyProjectBoardEntity.getId())
                    .title(studyProjectBoardEntity.getTitle())
                    .summary(studyProjectBoardEntity.getSummary())
                    .type(studyProjectBoardEntity.getType())
                    .tag(studyProjectBoardEntity.getTag())
                    .recruitmentTarget(studyProjectBoardEntity.getRecruitmentTarget())
                    .recruitmentNum(studyProjectBoardEntity.getRecruitmentNum())
                    .recruitmentStart(studyProjectBoardEntity.getActivityStart())
                    .recruitmentDeadline(studyProjectBoardEntity.getRecruitmentDeadline())
                    .build();
        }
    }
}

