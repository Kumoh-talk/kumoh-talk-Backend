package com.example.demo.application.recruitment_board.dto.response;

import com.example.demo.domain.recruitment_board.entity.RecruitmentFormAnswerInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentFormQuestionInfo;
import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
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
@Schema(name = "RecruitmentFormQuestionResponse", description = "모집 게시물 신청폼 응답")
public class RecruitmentFormQuestionResponse {
    @Schema(description = "신청폼 질문 id 정보", example = "3")
    private Long questionId;
    @Schema(description = "신청폼 질문 순서 정보", example = "1")
    private Integer number;
    @Schema(description = "신청폼 질문 내용 정보", example = "what is your hobby?")
    private String question;
    @Schema(description = "신청폼 질문 타입 정보[CHOICE, DESCRIPTION, CHECKBOX]", example = "DESCRIPTION")
    private QuestionType type;
    @Schema(description = "신청폼 질문 필수여부 정보", example = "true")
    private Boolean isEssential;
    @Schema(description = "신청폼 질문 선택형 답안 리스트")
    private List<RecruitmentFormAnswerResponse> answerList;

    public static RecruitmentFormQuestionResponse from(
            RecruitmentFormQuestionInfo recruitmentFormQuestionInfo) {
        return RecruitmentFormQuestionResponse.builder()
                .questionId(recruitmentFormQuestionInfo.getQuestionId())
                .number(recruitmentFormQuestionInfo.getNumber())
                .question(recruitmentFormQuestionInfo.getQuestion())
                .type(recruitmentFormQuestionInfo.getType())
                .isEssential(recruitmentFormQuestionInfo.getIsEssential())
                .answerList(recruitmentFormQuestionInfo.getAnswerList().stream()
                        .map(RecruitmentFormAnswerResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "RecruitmentFormAnswerResponse", description = "신청폼 질문 선택형 답안 응답")
    public static class RecruitmentFormAnswerResponse {
        @Schema(description = "선택형 답안 id 정보", example = "1")
        private Long answerId;
        @Schema(description = "선택형 답안 순서 정보", example = "1")
        private Integer number;
        @Schema(description = "선택형 답안 내용 정보", example = "answer1")
        private String answer;

        public static RecruitmentFormAnswerResponse from(
                RecruitmentFormAnswerInfo recruitmentFormAnswerInfo) {
            return RecruitmentFormAnswerResponse.builder()
                    .answerId(recruitmentFormAnswerInfo.getAnswerId())
                    .number(recruitmentFormAnswerInfo.getNumber())
                    .answer(recruitmentFormAnswerInfo.getAnswer())
                    .build();
        }
    }
}
