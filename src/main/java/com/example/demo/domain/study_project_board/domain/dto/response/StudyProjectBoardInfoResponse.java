package com.example.demo.domain.study_project_board.domain.dto.response;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardCategory;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardTag;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProjectBoardInfoResponse {
    private Long boardId;

    private String writerNickname;

    private String writerPhoneNumber;

    private String title;

    private String summary;

    private String content;

    private StudyProjectBoardCategory category;

    private StudyProjectBoardTag tag;

    private Status status;

    private String recruitmentTarget;

    private String recruitmentNum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime recruitmentStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime recruitmentDeadline;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activityStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activityFinish;

    private String activityCycle;

    public static StudyProjectBoardInfoResponse from(StudyProjectBoard studyProjectBoardEntity) {
        return StudyProjectBoardInfoResponse.builder()
                .boardId(studyProjectBoardEntity.getId())
                .writerNickname(studyProjectBoardEntity.getUser().getNickname())
                .writerPhoneNumber(studyProjectBoardEntity.getUser().getUserAdditionalInfo().getPhoneNumber())
                .title(studyProjectBoardEntity.getTitle())
                .summary(studyProjectBoardEntity.getSummary())
                .content(studyProjectBoardEntity.getContent())
                .category(studyProjectBoardEntity.getCategory())
                .tag(studyProjectBoardEntity.getTag())
                .status(studyProjectBoardEntity.getStatus())
                .recruitmentTarget(studyProjectBoardEntity.getRecruitmentTarget())
                .recruitmentNum(studyProjectBoardEntity.getRecruitmentNum())
                .recruitmentStart(studyProjectBoardEntity.getActivityStart())
                .recruitmentDeadline(studyProjectBoardEntity.getRecruitmentDeadline())
                .activityStart(studyProjectBoardEntity.getActivityStart())
                .activityFinish(studyProjectBoardEntity.getActivityFinish())
                .activityCycle(studyProjectBoardEntity.getActivityCycle())
                .build();
    }
}
