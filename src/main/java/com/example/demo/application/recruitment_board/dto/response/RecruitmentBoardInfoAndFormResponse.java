package com.example.demo.application.recruitment_board.dto.response;

import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardAndFormInfo;
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

    public static RecruitmentBoardInfoAndFormResponse from(RecruitmentBoardAndFormInfo recruitmentBoardInfo) {
        return RecruitmentBoardInfoAndFormResponse
                .builder()
                .board(RecruitmentBoardInfoResponse.from(recruitmentBoardInfo.getBoard()))
                .form(recruitmentBoardInfo.getForm().stream()
                        .map(RecruitmentFormQuestionResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
