package com.example.demo.domain.recruitment_board.domain.dto.request;

import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.global.aop.valid.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecruitmentBoardInfoRequest {
    @NotBlank(message = "제목을 작성해야합니다.")
    private String title;

    @NotBlank(message = "요약 내용을 작성해야합니다.")
    private String summary;

    @NotBlank(message = "내용을 작성해야합니다.")
    private String content;

    @ValidEnum(enumClass = RecruitmentBoardType.class, message = "게시물 타입은 STUDY, PROJECT 중 하나여야 합니다.")
    private RecruitmentBoardType type;

    @ValidEnum(enumClass = RecruitmentBoardTag.class, message = "태그는 FRONTEND, BACKEND, AI, MOBILE, SECURITY 중 하나여야 합니다.")
    private RecruitmentBoardTag tag;

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
