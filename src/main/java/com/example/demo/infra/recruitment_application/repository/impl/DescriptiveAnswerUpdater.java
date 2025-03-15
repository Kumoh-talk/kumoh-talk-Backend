package com.example.demo.infra.recruitment_application.repository.impl;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplicationDescriptiveAnswer;
import com.example.demo.infra.recruitment_application.repository.jpa.RecruitmentApplicationDescriptiveAnswerJpaRepository;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DescriptiveAnswerUpdater {
    private final RecruitmentApplicationDescriptiveAnswerJpaRepository descriptiveAnswerJpaRepository;

    public boolean updateDescriptiveAnswer(RecruitmentApplication application, RecruitmentBoard recruitmentBoard,
                                           RecruitmentApplicationAnswerInfo newApplicationAnswerInfo,
                                           List<RecruitmentApplicationDescriptiveAnswer> originDescriptiveAnswerList) {
        List<Long> toDeleteOriginAnswerIdList = new ArrayList<>();

        // 기존 답변 업데이트 및 삭제 처리
        boolean isUpdate = updateOrDeleteDescriptiveAnswers(originDescriptiveAnswerList, newApplicationAnswerInfo, toDeleteOriginAnswerIdList);

        // 새로운 답변 추가
        if (!isUpdate && !newApplicationAnswerInfo.getAnswerInfoList().isEmpty()) {
            isUpdate = addNewDescriptiveAnswer(application, recruitmentBoard, newApplicationAnswerInfo, originDescriptiveAnswerList);
        }

        // 삭제된 답변 처리
        descriptiveAnswerJpaRepository.hardDeleteAnswersByIds(toDeleteOriginAnswerIdList);
        return isUpdate;
    }

    private boolean updateOrDeleteDescriptiveAnswers(List<RecruitmentApplicationDescriptiveAnswer> originAnswers,
                                                     RecruitmentApplicationAnswerInfo newAnswerInfo,
                                                     List<Long> toDeleteIds) {
        boolean isUpdated = false;

        for (Iterator<RecruitmentApplicationDescriptiveAnswer> it = originAnswers.iterator(); it.hasNext(); ) {
            RecruitmentApplicationDescriptiveAnswer originAnswer = it.next();
            if (originAnswer.getRecruitmentFormQuestion().getId().equals(newAnswerInfo.getQuestionId())) {
                if (newAnswerInfo.getAnswerInfoList() == null ||
                        newAnswerInfo.getAnswerInfoList().isEmpty()) {
                    toDeleteIds.add(originAnswer.getId());
                    it.remove();
                } else {
                    originAnswer.update(newAnswerInfo.getAnswerInfoList().get(0));
                }
                isUpdated = true;
                break;
            }
        }
        return isUpdated;
    }

    private boolean addNewDescriptiveAnswer(RecruitmentApplication application, RecruitmentBoard recruitmentBoard,
                                            RecruitmentApplicationAnswerInfo newAnswerInfo, List<RecruitmentApplicationDescriptiveAnswer> originDescriptiveAnswerList) {

        RecruitmentFormQuestion matchQuestionEntity = findMatchingQuestion(recruitmentBoard, newAnswerInfo.getQuestionId());
        if (matchQuestionEntity.getType() != QuestionType.DESCRIPTION) {
            return false;
        }

        RecruitmentApplicationDescriptiveAnswer newAnswer = RecruitmentApplicationDescriptiveAnswer.of(
                newAnswerInfo.getAnswerInfoList().get(0),
                matchQuestionEntity,
                application
        );

        originDescriptiveAnswerList.add(newAnswer);
        descriptiveAnswerJpaRepository.save(newAnswer);
        return true;
    }

    public RecruitmentFormQuestion findMatchingQuestion(RecruitmentBoard recruitmentBoard, Long questionId) {
        for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
            if (recruitmentFormQuestion.getId().equals(questionId)) {
                return recruitmentFormQuestion;
            }
        }
        throw new IllegalArgumentException("question");
    }
}
