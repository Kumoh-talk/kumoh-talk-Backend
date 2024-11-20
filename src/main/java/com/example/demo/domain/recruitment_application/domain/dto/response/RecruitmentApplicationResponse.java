package com.example.demo.domain.recruitment_application.domain.dto.response;

import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantDescriptiveAnswer;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantOptionalAnswer;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentFormQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "RecruitmentApplicationResponse", description = "모집 게시물 신청서 응답")
public class RecruitmentApplicationResponse {
    @Schema(description = "신청자 id 정보", example = "1")
    private final Long applicantId;
    @Schema(description = "신청자의 신청서 정보 리스트")
    private final List<RecruitmentApplicantAnswerInfoResponse> applicantAnswer;

    public static RecruitmentApplicationResponse from(RecruitmentApplicant applicant,
                                                      RecruitmentBoard recruitmentBoard,
                                                      List<RecruitmentApplicantDescriptiveAnswer> saveApplicantDescriptiveAnswerList,
                                                      List<RecruitmentApplicantOptionalAnswer> saveApplicantOptionalAnswerList) {
        RecruitmentApplicationResponse response = RecruitmentApplicationResponse.builder()
                .applicantId(applicant.getId())
                .applicantAnswer(new ArrayList<>())
                .build();

        // 게시물의 질문에 해당하는 답이 존재하는지 차례대로 확인
        for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
            // 서술형 답변 중 존재하는지 확인
            boolean shouldContinue = false;
            for (RecruitmentApplicantDescriptiveAnswer descriptiveAnswer : saveApplicantDescriptiveAnswerList) {
                if (descriptiveAnswer.getRecruitmentFormQuestion().getId().equals(recruitmentFormQuestion.getId())) {
                    response.applicantAnswer.add(RecruitmentApplicantAnswerInfoResponse.from(descriptiveAnswer));
                    shouldContinue = true;
                    break;
                }
            }
            if (shouldContinue) {
                continue;
            }

            // 선택형 답변 중 존재하는지 확인
            // 체크박스의 경우 답변이 복수개 존재할 수도 있음
            RecruitmentApplicantAnswerInfoResponse currentQuestionResponse = null;
            for (RecruitmentApplicantOptionalAnswer optionalAnswer : saveApplicantOptionalAnswerList) {
                if (optionalAnswer.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getId().equals(recruitmentFormQuestion.getId())) {
                    if (currentQuestionResponse == null) {
                        currentQuestionResponse = RecruitmentApplicantAnswerInfoResponse.from(optionalAnswer);
                        response.applicantAnswer.add(currentQuestionResponse);
                    } else {
                        currentQuestionResponse.getAnswerList().add(RecruitmentApplicantAnswerInfoResponse.Answer.from(optionalAnswer));
                    }
                }
            }
            if (currentQuestionResponse != null) {
                currentQuestionResponse.getAnswerList().sort(Comparator.comparing(RecruitmentApplicantAnswerInfoResponse.Answer::getAnswerNumber));
                continue;
            }

            // 답변이 존재하지 않을 경우 신청자가 답변하지 않은 경우이므로 빈 답변 리스트를 넣음
            response.applicantAnswer.add(RecruitmentApplicantAnswerInfoResponse.from(recruitmentFormQuestion));
        }

        return response;
    }
}
