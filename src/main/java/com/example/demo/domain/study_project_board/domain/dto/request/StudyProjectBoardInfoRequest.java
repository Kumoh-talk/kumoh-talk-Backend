package com.example.demo.domain.study_project_board.domain.dto.request;

import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardCategory;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StudyProjectBoardInfoRequest {
    @NotBlank(message = "제목을 작성해야합니다.")
    private String title;

    @NotBlank(message = "요약 내용을 작성해야합니다.")
    private String summary;

    @NotBlank(message = "내용을 작성해야합니다.")
    private String content;

    @NotNull(message = "카테고리를 선택해야합니다.")
    private StudyProjectBoardCategory category;

    @NotNull(message = "태그를 선택해야합니다.")
    private StudyProjectBoardTag tag;

    @NotBlank(message = "신청 대상을 작성해야합니다.")
    private String recruitmentTarget;

    @NotNull(message = "모집 인원을 작성해야합니다.")
    private String recruitmentNum;

    @NotNull(message = "모집 마감일을 작성해야합니다.")
    private LocalDateTime recruitmentDeadline;

    @NotNull(message = "활동 시작일을 작성해야합니다.")
    private LocalDateTime activityStart;

    @NotNull(message = "활동 종료일를 작성해야합니다.")
    private LocalDateTime activityFinish;

    @NotBlank(message = "활동 주기를 작성해야합니다.")
    private String activityCycle;
}
