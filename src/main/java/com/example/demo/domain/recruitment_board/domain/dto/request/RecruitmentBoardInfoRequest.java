package com.example.demo.domain.recruitment_board.domain.dto.request;

import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Schema(description = "모집 게시물 정보 요청")
public class RecruitmentBoardInfoRequest {
    @Schema(description = "모집 게시물 제목 정보", example = "board title")
    @NotBlank(message = "제목을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "제목 최대 길이는 50글자 입니다.")
    private String title;

    @Schema(description = "모집 게시물 요약 내용 정보", example = "board summary")
    @NotBlank(message = "요약 내용을 작성해야합니다.")
    @Size(min = 1, max = 100, message = "요약 최대 길이는 100글자 입니다.")
    private String summary;

    @Schema(description = "모집 게시물 주최자 정보", example = "board host")
    @NotBlank(message = "주최자 내용을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "주최자 최대 길이는 50글자 입니다.")
    private String host;

    @Schema(description = "모집 게시물 내용 정보", example = "board content")
    @NotBlank(message = "본문 내용을 작성해야합니다.")
    @Size(min = 1, max = 1000, message = "본문 내용 최대 길이는 1000글자 입니다.")
    private String content;

    @Schema(description = "모집 게시물 타입 정보[study, project, mentoring]", example = "study")
    @NotNull(message = "타입을 선택해야합니다.")
    private RecruitmentBoardType type;

    @Schema(description = "모집 게시물 태그 정보[frontend, backend, ai, mobile, security, etc]", example = "frontend")
    @NotNull(message = "태그를 선택해야합니다.")
    private RecruitmentBoardTag tag;

    @Schema(description = "모집 게시물 모집 대상 정보", example = "people using spring")
    @NotBlank(message = "신청 대상을 작성해야합니다.")
    @Size(min = 1, max = 50, message = "모집 대상 최대 길이는 50글자 입니다.")
    private String recruitmentTarget;

    @Schema(description = "모집 게시물 모집 인원", example = "10")
    @NotNull(message = "모집 인원을 작성해야합니다.")
    private Integer recruitmentNum;

    @Schema(description = "모집 게시물 현재 인원", example = "3")
    @NotNull(message = "현재 인원을 작성해야합니다.")
    private Integer currentMemberNum;

    @Schema(description = "모집 마감 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @NotNull(message = "모집 마감일을 작성해야합니다.")
    private LocalDateTime recruitmentDeadline;

    @Schema(description = "활동 시작 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @NotNull(message = "활동 시작일을 작성해야합니다.")
    private LocalDateTime activityStart;

    @Schema(description = "활동 종료 날짜 및 시간", example = "\"2024-11-18T17:09:25\"")
    @NotNull(message = "활동 종료일를 작성해야합니다.")
    private LocalDateTime activityFinish;

    @Schema(description = "활동 주기", example = "twice a week")
    @NotBlank(message = "활동 주기를 작성해야합니다.")
    @Size(min = 1, max = 50, message = "활동 주기 최대 길이는 50글자 입니다.")
    private String activityCycle;
}
