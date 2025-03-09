package com.example.demo.infra.recruitment_application.repository.impl;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplicationOptionalAnswer;
import com.example.demo.infra.recruitment_application.repository.jpa.RecruitmentApplicationOptionalAnswerJpaRepository;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormAnswer;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OptionalAnswerUpdater {
    private final RecruitmentApplicationOptionalAnswerJpaRepository optionalAnswerJpaRepository;
    private final DescriptiveAnswerUpdater descriptiveAnswerUpdater;

    public boolean updateOptionalAnswer(RecruitmentApplication application, RecruitmentBoard recruitmentBoard,
                                        RecruitmentApplicationAnswerInfo newApplicationAnswerInfo,
                                        List<RecruitmentApplicationOptionalAnswer> originOptionalAnswerList) {
        List<Long> toDeleteOriginAnswerIdList = new ArrayList<>();
        List<Integer> originAnswerNumberList = new ArrayList<>();
        boolean isUpdate = updateOrDeleteOptionalAnswers(originOptionalAnswerList, newApplicationAnswerInfo, toDeleteOriginAnswerIdList, originAnswerNumberList);

        // 새로운 선택형 답변 추가
        if (shouldAddOptionalAnswers(originAnswerNumberList, newApplicationAnswerInfo)) {
            addNewOptionalAnswers(application, recruitmentBoard, newApplicationAnswerInfo, originOptionalAnswerList, originAnswerNumberList);
            isUpdate = true;
        }

        optionalAnswerJpaRepository.hardDeleteAnswersByIds(toDeleteOriginAnswerIdList);
        return isUpdate;
    }

    private boolean updateOrDeleteOptionalAnswers(List<RecruitmentApplicationOptionalAnswer> originAnswers,
                                                  RecruitmentApplicationAnswerInfo newAnswerInfo,
                                                  List<Long> toDeleteIds, List<Integer> originAnswerNumberList) {
        boolean isUpdated = false;

        for (Iterator<RecruitmentApplicationOptionalAnswer> it = originAnswers.iterator(); it.hasNext(); ) {
            RecruitmentApplicationOptionalAnswer originAnswer = it.next();
            if (originAnswer.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getId().equals(newAnswerInfo.getQuestionId())) {
                if (newAnswerInfo.getAnswerInfoList() != null
                        && newAnswerInfo.getAnswerInfoList().stream()
                        .anyMatch(answerInfo -> answerInfo.getAnswerNumber().equals(originAnswer.getRecruitmentFormAnswer().getNumber()))) {

                    originAnswerNumberList.add(originAnswer.getRecruitmentFormAnswer().getNumber());
                    continue;
                } else {
                    toDeleteIds.add(originAnswer.getId());
                    it.remove();
                }
                isUpdated = true;
            }
        }
        return isUpdated;
    }

    private boolean shouldAddOptionalAnswers(List<Integer> originAnswers,
                                             RecruitmentApplicationAnswerInfo newAnswerInfo) {
        return originAnswers.size() < newAnswerInfo.getAnswerInfoList().size();
    }

    private void addNewOptionalAnswers(
            RecruitmentApplication application,
            RecruitmentBoard recruitmentBoard,
            RecruitmentApplicationAnswerInfo newApplicationAnswerInfo,
            List<RecruitmentApplicationOptionalAnswer> originOptionalAnswerList,
            List<Integer> originAnswerNumberList) {

        List<RecruitmentApplicationOptionalAnswer> newAnswers = newApplicationAnswerInfo.getAnswerInfoList().stream()
                .filter(newAnswerInfo -> originAnswerNumberList.stream()
                        .noneMatch(answerNumber -> answerNumber.equals(newAnswerInfo.getAnswerNumber())))
                .map(newAnswerInfo -> {
                    RecruitmentFormQuestion formQuestion = descriptiveAnswerUpdater.findMatchingQuestion(recruitmentBoard, newApplicationAnswerInfo.getQuestionId());
                    RecruitmentFormAnswer formAnswer = findMatchingAnswer(formQuestion, newAnswerInfo.getAnswerNumber());

                    return RecruitmentApplicationOptionalAnswer.of(formAnswer, application);
                }).collect(Collectors.toList());

        originOptionalAnswerList.addAll(newAnswers);
        optionalAnswerJpaRepository.saveAll(newAnswers);
    }

    public RecruitmentFormAnswer findMatchingAnswer(RecruitmentFormQuestion formQuestion, Integer answerNumber) {
        for (RecruitmentFormAnswer formAnswer : formQuestion.getRecruitmentFormAnswerList()) {
            if (formAnswer.getNumber().equals(answerNumber)) {
                return formAnswer;
            }
        }
        throw new IllegalArgumentException("answer");
    }
}
