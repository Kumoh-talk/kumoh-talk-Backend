package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
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
public class RecruitmentBoardInfoAndFormResponse {

    private RecruitmentBoardInfoResponse board;

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
