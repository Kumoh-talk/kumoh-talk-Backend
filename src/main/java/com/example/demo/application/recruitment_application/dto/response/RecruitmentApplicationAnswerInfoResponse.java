package com.example.demo.application.recruitment_application.dto.response;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentApplicantAnswerInfoResponse", description = "신청자의 신청서 정보 응답")
public class RecruitmentApplicationAnswerInfoResponse {
    @Schema(description = "신청폼 질문 id 정보", example = "3")
    private Long questionId;
    @Schema(description = "신청폼 질문 순서 정보", example = "1")
    private Integer questionNumber;
    @Schema(description = "신청폼 질문 내용 정보", example = "what is your hobby?")
    private String question;
    @Schema(description = "질문에 해당하는 신청서의 답변 리스트")
    private List<Answer> answerList;

    public static RecruitmentApplicationAnswerInfoResponse from(RecruitmentApplicationAnswerInfo recruitmentApplicationAnswerInfo) {
        return RecruitmentApplicationAnswerInfoResponse.builder()
                .questionId(recruitmentApplicationAnswerInfo.getQuestionId())
                .questionNumber(recruitmentApplicationAnswerInfo.getQuestionNumber())
                .question(recruitmentApplicationAnswerInfo.getQuestion())
                .answerList(recruitmentApplicationAnswerInfo.getAnswerInfoList().stream()
                        .map(Answer::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Schema(name = "Answer", description = "신청서의 답변 응답")
    public static class Answer {
        @Schema(description = "신청서 답변 id 정보", example = "1")
        private Long answerId;
        @Schema(description = "신청서 답변 순서 정보", example = "3")
        private Integer answerNumber;
        @Schema(description = "신청서 답변 내용 정보", example = "answer")
        private String answer;

        public static Answer from(RecruitmentApplicationAnswerInfo.AnswerInfo answerInfo) {
            return Answer.builder()
                    .answerId(answerInfo.getAnswerId())
                    .answerNumber(answerInfo.getAnswerNumber())
                    .answer(answerInfo.getAnswer())
                    .build();
        }
    }
}
