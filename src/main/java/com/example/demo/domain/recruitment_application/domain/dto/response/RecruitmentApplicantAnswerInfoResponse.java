package com.example.demo.domain.recruitment_application.domain.dto.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantDescriptiveAnswer;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantOptionalAnswer;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentApplicantAnswerInfoResponse", description = "신청자의 신청서 정보 응답")
public class RecruitmentApplicantAnswerInfoResponse {
    @Schema(description = "신청폼 질문 id 정보", example = "3")
    private Long questionId;
    @Schema(description = "신청폼 질문 순서 정보", example = "1")
    private Integer questionNumber;
    @Schema(description = "신청폼 질문 내용 정보", example = "what is your hobby?")
    private String question;
    @Schema(description = "질문에 해당하는 신청서의 답변 리스트")
    private List<Answer> answerList;

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

        public static Answer from(RecruitmentApplicantDescriptiveAnswer applicantAnswerEntity) {
            return Answer.builder()
                    .answerId(applicantAnswerEntity.getId())
                    .answerNumber(null)
                    .answer(applicantAnswerEntity.getAnswer())
                    .build();
        }

        public static Answer from(RecruitmentApplicantOptionalAnswer applicantAnswerEntity) {
            return Answer.builder()
                    .answerId(applicantAnswerEntity.getId())
                    .answerNumber(applicantAnswerEntity.getRecruitmentFormAnswer().getNumber())
                    .answer(applicantAnswerEntity.getRecruitmentFormAnswer().getAnswer())
                    .build();
        }
    }

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentApplicantDescriptiveAnswer applicantAnswerEntity) {
        RecruitmentApplicantAnswerInfoResponse response = RecruitmentApplicantAnswerInfoResponse.builder()
                .questionId(applicantAnswerEntity.getRecruitmentFormQuestion().getId())
                .questionNumber(applicantAnswerEntity.getRecruitmentFormQuestion().getNumber())
                .question(applicantAnswerEntity.getRecruitmentFormQuestion().getQuestion())
                .answerList(new ArrayList<>())
                .build();
        response.getAnswerList().add(Answer.from(applicantAnswerEntity));
        return response;
    }

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentApplicantOptionalAnswer applicantAnswerEntity) {
        RecruitmentApplicantAnswerInfoResponse response = RecruitmentApplicantAnswerInfoResponse.builder()
                .questionId(applicantAnswerEntity.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getId())
                .questionNumber(applicantAnswerEntity.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getNumber())
                .question(applicantAnswerEntity.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getQuestion())
                .answerList(new ArrayList<>())
                .build();
        response.getAnswerList().add(Answer.from(applicantAnswerEntity));
        return response;
    }

    public static RecruitmentApplicantAnswerInfoResponse from(RecruitmentFormQuestion recruitmentFormQuestion) {
        RecruitmentApplicantAnswerInfoResponse response = RecruitmentApplicantAnswerInfoResponse.builder()
                .questionId(recruitmentFormQuestion.getId())
                .questionNumber(recruitmentFormQuestion.getNumber())
                .question(recruitmentFormQuestion.getQuestion())
                .answerList(new ArrayList<>())
                .build();
        return response;
    }
}
