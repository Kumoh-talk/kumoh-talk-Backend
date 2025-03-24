package com.example.demo.infra.recruitment_application.repository.impl;

import com.example.demo.domain.recruitment_application.entity.MyRecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicationRepository;
import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplicationDescriptiveAnswer;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplicationOptionalAnswer;
import com.example.demo.infra.recruitment_application.repository.jpa.RecruitmentApplicationDescriptiveAnswerJpaRepository;
import com.example.demo.infra.recruitment_application.repository.jpa.RecruitmentApplicationJpaRepository;
import com.example.demo.infra.recruitment_application.repository.jpa.RecruitmentApplicationOptionalAnswerJpaRepository;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormAnswer;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentBoardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecruitmentApplicationImpl implements RecruitmentApplicationRepository {
    private final UserJpaRepository userJpaRepository;
    private final RecruitmentBoardJpaRepository recruitmentBoardJpaRepository;
    private final RecruitmentApplicationJpaRepository recruitmentApplicationJpaRepository;
    private final RecruitmentApplicationDescriptiveAnswerJpaRepository descriptiveAnswerJpaRepository;
    private final RecruitmentApplicationOptionalAnswerJpaRepository optionalAnswerJpaRepository;

    private final DescriptiveAnswerUpdater descriptiveAnswerUpdater;
    private final OptionalAnswerUpdater optionalAnswerUpdater;

    @Override
    public boolean existsByUserIdAndRecruitmentBoardId(Long userId, Long recruitmentBoardId) {
        return recruitmentApplicationJpaRepository.existsByUserIdAndRecruitmentBoardId(userId, recruitmentBoardId);
    }

    @Override
    public RecruitmentApplicationInfo post(RecruitmentApplicationInfo recruitmentApplicationInfo, Long recruitmentBoardId) {
        User user = userJpaRepository.findById(recruitmentApplicationInfo.getUserId()).get();
        RecruitmentBoard recruitmentBoard = recruitmentBoardJpaRepository.findById(recruitmentBoardId).get();

        RecruitmentApplication recruitmentApplication = RecruitmentApplication.of(user, recruitmentBoard);
        separateAnswerList(recruitmentApplication, recruitmentBoard.getRecruitmentFormQuestionList(), recruitmentApplicationInfo);

        RecruitmentApplication saved = recruitmentApplicationJpaRepository.save(recruitmentApplication);
        return saved.toApplicationDomain(
                recruitmentBoard.getRecruitmentFormQuestionList(),
                saved.getDescriptiveAnswerList(),
                saved.getOptionalAnswerList());
    }

    @Override
    public RecruitmentApplicationInfo patch(RecruitmentApplicationInfo originApplicationInfo, RecruitmentApplicationInfo newApplicationInfo) {
        RecruitmentBoard recruitmentBoard = recruitmentBoardJpaRepository.findById(originApplicationInfo.getRecruitmentBoardId()).get();
        RecruitmentApplication recruitmentApplication = recruitmentApplicationJpaRepository.findById(originApplicationInfo.getApplicationId()).get();

        List<RecruitmentApplicationDescriptiveAnswer> descriptiveAnswerList = descriptiveAnswerJpaRepository.findByRecruitmentApplicationIdWithQuestion(recruitmentApplication.getId());
        List<RecruitmentApplicationOptionalAnswer> optionalAnswerList = optionalAnswerJpaRepository.findByRecruitmentApplicationIdWithAnswerAndQuestion(recruitmentApplication.getId());

        Iterator<RecruitmentApplicationAnswerInfo> newApplicationAnswerInfoIterator = newApplicationInfo.getAnswerInfoList().iterator();
        while (newApplicationAnswerInfoIterator.hasNext()) {
            RecruitmentApplicationAnswerInfo newApplicationAnswerInfo = newApplicationAnswerInfoIterator.next();

            // TODO : JDBCTemplate을 사용한 벌크 insert, update 적용?
            if (!descriptiveAnswerUpdater.updateDescriptiveAnswer(recruitmentApplication, recruitmentBoard, newApplicationAnswerInfo, descriptiveAnswerList)) {
                optionalAnswerUpdater.updateOptionalAnswer(recruitmentApplication, recruitmentBoard, newApplicationAnswerInfo, optionalAnswerList);
            }
        }

        return recruitmentApplication.toApplicationDomain(
                recruitmentBoard.getRecruitmentFormQuestionList(),
                descriptiveAnswerList,
                optionalAnswerList);
    }

    @Override
    public void delete(RecruitmentApplicationInfo recruitmentApplicationInfo) {
        RecruitmentApplication recruitmentApplication = recruitmentApplicationJpaRepository.findById(recruitmentApplicationInfo.getApplicationId()).get();
        recruitmentApplicationJpaRepository.delete(recruitmentApplication);
    }

    @Override
    public Page<RecruitmentApplicationInfo> getPageByRecruitmentBoardIdOrderByCreatedAtDesc(Pageable pageable, Long recruitmentBoardId) {
        return recruitmentApplicationJpaRepository.findPageByRecruitmentBoardIdOrderByCreatedAtDesc(pageable, recruitmentBoardId)
                .map(RecruitmentApplication::toApplicantDomain);
    }

    @Override
    public Page<MyRecruitmentApplicationInfo> getPageByUserIdWithRecruitmentBoard(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        return recruitmentApplicationJpaRepository.findPageByUserIdWithRecruitmentBoard(userId, pageable, boardType)
                .map(RecruitmentApplication::toMyApplicationDomain);
    }

    @Override
    public Optional<RecruitmentApplicationInfo> getByIdWithAnswerList(Long applicationId, Long recruitmentBoardId) {
        RecruitmentBoard recruitmentBoard = recruitmentBoardJpaRepository.findById(recruitmentBoardId).get();
        RecruitmentApplication recruitmentApplication = recruitmentApplicationJpaRepository.findByIdWithUser(applicationId).orElse(null);

        if (recruitmentApplication == null) {
            return Optional.empty();
        } else {
            List<RecruitmentApplicationDescriptiveAnswer> descriptiveAnswerList = descriptiveAnswerJpaRepository.findByRecruitmentApplicationIdWithQuestion(applicationId);
            List<RecruitmentApplicationOptionalAnswer> optionalAnswerList = optionalAnswerJpaRepository.findByRecruitmentApplicationIdWithAnswerAndQuestion(applicationId);

            return Optional.of(recruitmentApplication.toApplicationDomain(
                    recruitmentBoard.getRecruitmentFormQuestionList(),
                    descriptiveAnswerList,
                    optionalAnswerList));
        }
    }

    @Override
    public Optional<RecruitmentApplicationInfo> getById(Long applicationId) {
        return recruitmentApplicationJpaRepository.findByIdWithUser(applicationId)
                .map(RecruitmentApplication::toApplicantDomain);
    }

    @Override
    public Optional<RecruitmentApplicationInfo> getByIdWithBoard(Long applicationId) {
        return recruitmentApplicationJpaRepository.findByIdWithUserAndBoard(applicationId)
                .map(RecruitmentApplication::toApplicantDomainWIthBoard);
    }

    private void separateAnswerList(
            RecruitmentApplication recruitmentApplication,
            List<RecruitmentFormQuestion> recruitmentFormQuestionList,
            RecruitmentApplicationInfo recruitmentApplicationInfo) {

        // 입력받은 답변 Map 형태로 변환
        Map<Long, RecruitmentApplicationAnswerInfo> applicationAnswerInfoMap =
                recruitmentApplicationInfo.getAnswerInfoList().stream()
                        .collect(Collectors.toMap(RecruitmentApplicationAnswerInfo::getQuestionId, Function.identity()));

        // 모집 게시물의 질문을 순회하며 답변을 분리
        for (RecruitmentFormQuestion question : recruitmentFormQuestionList) {
            RecruitmentApplicationAnswerInfo applicationAnswerInfo = applicationAnswerInfoMap.get(question.getId());

            if (applicationAnswerInfo != null) {
                if (question.getType() == QuestionType.DESCRIPTION) {
                    recruitmentApplication.getDescriptiveAnswerList().addAll(applicationAnswerInfo.getAnswerInfoList().stream()
                            .map(answer -> RecruitmentApplicationDescriptiveAnswer.of(answer, question, recruitmentApplication))
                            .collect(Collectors.toList()));
                } else {
                    for (RecruitmentApplicationAnswerInfo.AnswerInfo answer : applicationAnswerInfo.getAnswerInfoList()) {
                        RecruitmentFormAnswer formAnswer = optionalAnswerUpdater.findMatchingAnswer(question, answer.getAnswerNumber());

                        recruitmentApplication.getOptionalAnswerList().add(RecruitmentApplicationOptionalAnswer.of(formAnswer, recruitmentApplication));
                    }
                }
            } else {
                throw new IllegalArgumentException("question");
            }
        }
    }
}
