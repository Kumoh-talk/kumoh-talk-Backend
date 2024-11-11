package com.example.demo.domain.recruitment_board.domain.dto.request;

import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecruitmentBoardInfoRequest {
    @NotBlank(message = "제목을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "제목 최대 길이는 50글자 입니다.")
    private String title;

    @NotBlank(message = "요약 내용을 작성해야합니다.")
    @Size(min = 1, max = 100, message = "요약 최대 길이는 100글자 입니다.")
    private String summary;

    @NotBlank(message = "주최자 내용을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "주최자 최대 길이는 50글자 입니다.")
    private String host;

    @NotBlank(message = "본문 내용을 작성해야합니다.")
    @Size(min = 1, max = 1000, message = "본문 내용 최대 길이는 1000글자 입니다.")
    private String content;

    @NotNull(message = "타입을 선택해야합니다.")
    private RecruitmentBoardType type;

    @NotNull(message = "태그를 선택해야합니다.")
    private RecruitmentBoardTag tag;

    @NotBlank(message = "신청 대상을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "모집 대상 최대 길이는 50글자 입니다.")
    private String recruitmentTarget;

    @NotNull(message = "모집 인원을 작성해야합니다.")
    private Integer recruitmentNum;

    @NotNull(message = "현재 인원을 작성해야합니다.")
    private Integer currentMemberNum;

    @NotNull(message = "모집 마감일을 작성해야합니다.")
    private LocalDateTime recruitmentDeadline;

    @NotNull(message = "활동 시작일을 작성해야합니다.")
    private LocalDateTime activityStart;

    @NotNull(message = "활동 종료일를 작성해야합니다.")
    private LocalDateTime activityFinish;

    @NotBlank(message = "활동 주기를 작성해야합니다.")
    @Size(min = 1, max = 50, message = "활동 주기 최대 길이는 50글자 입니다.")
    private String activityCycle;
}
