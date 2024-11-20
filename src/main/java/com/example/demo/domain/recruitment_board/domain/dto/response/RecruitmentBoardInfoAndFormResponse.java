package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentBoardInfoAndFormResponse", description = "모집 게시물 응답")
public class RecruitmentBoardInfoAndFormResponse {
    @Schema(description = "모집 게시물 정보")
    private RecruitmentBoardInfoResponse board;

    @Schema(description = "모집 게시물 신청폼")
    private List<RecruitmentFormQuestionResponse> form;

    public static RecruitmentBoardInfoAndFormResponse from(RecruitmentBoard recruitmentBoard) {
        return RecruitmentBoardInfoAndFormResponse
                .builder()
                .board(RecruitmentBoardInfoResponse.from(recruitmentBoard))
                .form(recruitmentBoard.getRecruitmentFormQuestionList().stream()
                        .map(RecruitmentFormQuestionResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
