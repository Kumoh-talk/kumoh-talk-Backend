package com.example.demo.application.recruitment_application.dto.request;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "모집 게시물 신청 요청")
public class RecruitmentApplicationRequest {

    @Schema(description = "신청서 내용 정보 리스트")
    @NotNull
    @Valid
    private List<RecruitmentApplicationInfoRequest> application;

    public RecruitmentApplicationInfo toDomain(Long applicationId, Long userId, Long recruitmentBoardId) {
        return RecruitmentApplicationInfo.builder()
                .applicationId(applicationId)
                .userId(userId)
                .recruitmentBoardId(recruitmentBoardId)
                .answerInfoList(this.application.stream()
                        .map(RecruitmentApplicationInfoRequest::toDomain)
                        .toList())
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "신청서 내용 정보")
    public static class RecruitmentApplicationInfoRequest {
        @Schema(description = "질문 id 정보", example = "3")
        @NotNull(message = "대상 질문 id를 입력해야합니다.")
        private Long questionId;

        @Schema(description = "질문에 해당하는 답변 리스트")
        @NotNull(message = "답변을 입력해야합니다.")
        @Valid
        private List<RecruitmentApplicationAnswerInfoRequest> answerList;

        public RecruitmentApplicationAnswerInfo toDomain() {
            return RecruitmentApplicationAnswerInfo.builder()
                    .questionId(this.questionId)
                    .answerInfoList(this.answerList.stream()
                            .map(RecruitmentApplicationAnswerInfoRequest::toDomain)
                            .toList())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "신청서 답변 정보")
    public static class RecruitmentApplicationAnswerInfoRequest {
        @Schema(description = "답변 순서 정보(description 질문의 경우 null 입력)", example = "3")
        private Integer number;

        @Schema(description = "답변 내용 정보(choice, checkbox 질문의 경우 null 입력)", example = "answer!")
        @Size(min = 1, max = 1000, message = "답변 최대 길이는 1000글자 입니다.")
        private String answer;

        public RecruitmentApplicationAnswerInfo.AnswerInfo toDomain() {
            return RecruitmentApplicationAnswerInfo.AnswerInfo.builder()
                    .answerNumber(this.number)
                    .answer(this.answer)
                    .build();
        }
    }
}
