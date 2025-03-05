package com.example.demo.domain.recruitment_application.service;

import com.example.demo.domain.recruitment_application.domain.dto.request.RecruitmentApplicationRequest;
import com.example.demo.domain.recruitment_application.domain.dto.response.MyRecruitmentApplicationPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicantPageResponse;
import com.example.demo.domain.recruitment_application.domain.dto.response.RecruitmentApplicationResponse;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantDescriptiveAnswer;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicantOptionalAnswer;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicantDescriptiveAnswerRepository;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicantOptionalAnswerRepository;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicantRepository;
import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.service.RecruitmentBoardService;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormAnswer;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentBoardJpaRepository;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormAnswerJpaRepository;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormQuestionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentApplicationService {
    private final UserService userService;
    private final RecruitmentBoardService recruitmentBoardService;

    private final RecruitmentBoardJpaRepository recruitmentBoardRepository;
    private final RecruitmentFormAnswerJpaRepository recruitmentFormAnswerRepository;
    private final RecruitmentFormQuestionJpaRepository recruitmentFormQuestionRepository;
    private final RecruitmentApplicantRepository recruitmentApplicantRepository;
    private final RecruitmentApplicantDescriptiveAnswerRepository recruitmentApplicantDescriptiveAnswerRepository;
    private final RecruitmentApplicantOptionalAnswerRepository recruitmentApplicantOptionalAnswerRepository;


    @Transactional
    public RecruitmentApplicationResponse createApplication(Long userId, Long recruitmentBoardId, RecruitmentApplicationRequest request) {
        User user = userService.validateUser(userId);
        RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.findByIdByFetchingQuestionList(recruitmentBoardId).orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        recruitmentBoardService.validateDeadLine(userId, recruitmentBoard.toBoardInfoDomain());
        validateEssential(recruitmentBoard, request);
        if (recruitmentApplicantRepository.existsByUser_IdAndRecruitmentBoard_Id(userId, recruitmentBoardId)) {
            throw new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_EXIST);
        }

        RecruitmentApplicant applicant = RecruitmentApplicant.from(recruitmentBoard, user);

        List<RecruitmentApplicantDescriptiveAnswer> applicantDescriptiveAnswerList = new ArrayList<>();
        List<RecruitmentApplicantOptionalAnswer> applicantOptionalAnswerList = new ArrayList<>();
        transformToEntityFactory(request, recruitmentBoard, applicant, applicantDescriptiveAnswerList, applicantOptionalAnswerList);

        RecruitmentApplicant saveApplicant = recruitmentApplicantRepository.save(applicant);
        List<RecruitmentApplicantDescriptiveAnswer> saveApplicantDescriptiveAnswerList =
                recruitmentApplicantDescriptiveAnswerRepository.saveAll(applicantDescriptiveAnswerList);
        List<RecruitmentApplicantOptionalAnswer> saveApplicantOptinalAnswerList =
                recruitmentApplicantOptionalAnswerRepository.saveAll(applicantOptionalAnswerList);

        return RecruitmentApplicationResponse.from(saveApplicant, recruitmentBoard, saveApplicantDescriptiveAnswerList, saveApplicantOptinalAnswerList);
    }

    @Transactional(readOnly = true)
    public RecruitmentApplicantPageResponse getApplicantList(
            Long userId,
            Pageable pageable,
            Long recruitmentBoardId,
            boolean isAuthorized) {
        RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.findById(recruitmentBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));

        if (!isAuthorized) {
            if (!recruitmentBoard.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        Page<RecruitmentApplicant> recruitmentApplicantList = recruitmentApplicantRepository.findByRecruitmentBoard_IdOrderByCreatedAtDesc(pageable, recruitmentBoardId);

        return RecruitmentApplicantPageResponse.from(recruitmentApplicantList);
    }

    @Transactional(readOnly = true)
    public RecruitmentApplicationResponse getApplicationInfo(Long userId, Long recruitmentBoardId, Long applicantId, boolean isAuthorized) {
        RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.findByIdByFetchingQuestionList(recruitmentBoardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        if (!isAuthorized) {
            if (!recruitmentBoard.getUser().getId().equals(userId)) {
                throw new ServiceException(ErrorCode.ACCESS_DENIED);
            }
        }

        RecruitmentApplicant applicant = recruitmentApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        List<RecruitmentApplicantDescriptiveAnswer> recruitmentApplicantDescriptiveAnswerList = recruitmentApplicantDescriptiveAnswerRepository.findByRecruitmentApplicant_IdFetchQuestion(applicantId);
        List<RecruitmentApplicantOptionalAnswer> recruitmentApplicantOptionalAnswerList = recruitmentApplicantOptionalAnswerRepository.findByRecruitmentApplicant_IdFetchAnswerAndQuestion(applicantId);

        return RecruitmentApplicationResponse.from(applicant, recruitmentBoard, recruitmentApplicantDescriptiveAnswerList, recruitmentApplicantOptionalAnswerList);
    }

    @Transactional
    public RecruitmentApplicationResponse updateApplication(Long userId, Long applicantId, RecruitmentApplicationRequest request) {
        userService.validateUser(userId);
        RecruitmentApplicant applicant = recruitmentApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        if (!userId.equals(applicant.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
        RecruitmentBoard recruitmentBoard = recruitmentBoardRepository.findByIdByFetchingQuestionList(applicant.getRecruitmentBoard().getId())
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        recruitmentBoardService.validateDeadLine(userId, recruitmentBoard.toBoardInfoDomain());
        validateEssential(recruitmentBoard, request);

        List<RecruitmentApplicantDescriptiveAnswer> recruitmentApplicantDescriptiveAnswerList = recruitmentApplicantDescriptiveAnswerRepository.findByRecruitmentApplicant_IdFetchQuestion(applicant.getId());
        List<RecruitmentApplicantOptionalAnswer> recruitmentApplicantOptionalAnswerList = recruitmentApplicantOptionalAnswerRepository.findByRecruitmentApplicant_IdFetchAnswerAndQuestion(applicant.getId());

        List<Long> toDeleteDescriptiveAnswerIdList = new ArrayList<>();
        List<Long> toDeleteOptionalAnswerIdList = new ArrayList<>();

        Iterator<RecruitmentApplicationRequest.RecruitmentApplicantApplicationRequest> applicationRequestIterator = request.getApplication().iterator();
        while (applicationRequestIterator.hasNext()) {
            RecruitmentApplicationRequest.RecruitmentApplicantApplicationRequest applicationRequest = applicationRequestIterator.next();

            // TODO : JDBCTemplate을 사용한 벌크 insert, update 적용?
            if (!updateDescriptiveAnswer(applicant, recruitmentBoard, recruitmentApplicantDescriptiveAnswerList, applicationRequest, toDeleteDescriptiveAnswerIdList)) {
                updateOptionalAnswer(applicant, recruitmentBoard, recruitmentApplicantOptionalAnswerList, applicationRequest, toDeleteOptionalAnswerIdList);
            }
        }

        recruitmentApplicantDescriptiveAnswerRepository.hardDeleteAnswersByIds(toDeleteDescriptiveAnswerIdList);
        recruitmentApplicantOptionalAnswerRepository.hardDeleteAnswersByIds(toDeleteOptionalAnswerIdList);

        // TODO : save 직후에 find 해도 반영이 될까?
        recruitmentApplicantDescriptiveAnswerList = recruitmentApplicantDescriptiveAnswerRepository.findByRecruitmentApplicant_IdFetchQuestion(applicantId);
        recruitmentApplicantOptionalAnswerList = recruitmentApplicantOptionalAnswerRepository.findByRecruitmentApplicant_IdFetchAnswerAndQuestion(applicantId);

        return RecruitmentApplicationResponse.from(applicant, recruitmentBoard, recruitmentApplicantDescriptiveAnswerList, recruitmentApplicantOptionalAnswerList);
    }

    public boolean updateDescriptiveAnswer(RecruitmentApplicant applicant, RecruitmentBoard recruitmentBoard,
                                           List<RecruitmentApplicantDescriptiveAnswer> recruitmentApplicantDescriptiveAnswerList,
                                           RecruitmentApplicationRequest.RecruitmentApplicantApplicationRequest applicationRequest,
                                           List<Long> toDeleteDescriptiveAnswerIdList) {

        boolean isUpdate = false;
        Iterator<RecruitmentApplicantDescriptiveAnswer> descriptiveAnswerIterator = recruitmentApplicantDescriptiveAnswerList.iterator();
        // 신청자가 이전에 답변한 서술형 답변 리스트 순차 탐색
        while (descriptiveAnswerIterator.hasNext()) {
            RecruitmentApplicantDescriptiveAnswer descriptiveAnswer = descriptiveAnswerIterator.next();

            // 요청에서 수정한 답변이 이전에 답변한 적 있는 질문의 답변인 경우
            // 수정 답변이 빈 리스트 -> 답변하지 않는 경우이므로 이전 질문을 삭제한다.
            // 수정 답변이 빈 리스트가 아닌 경우 -> 이전 답변 데이터에서 답변 내용을 수정
            if (descriptiveAnswer.getRecruitmentFormQuestion().getId().equals(applicationRequest.getQuestionId())) {
                if (applicationRequest.getAnswerList().isEmpty()) {
                    toDeleteDescriptiveAnswerIdList.add(descriptiveAnswer.getId());
                    descriptiveAnswerIterator.remove();
                } else {
                    descriptiveAnswer.updateFromRequest(applicationRequest.getAnswerList().get(0));
                }
                isUpdate = true;
                break;
            }
        }

        // 예전 신청서의 서술형 답변 리스트에 요청한 답변 질문이 없는 경우 + 요청 답변이 빈 리스트가 아닌 경우(답변을 한 경우)
        // 요청 답변의 질문이 서술형 타입 질문이라면 새로운 답변 데이터를 추가한다.
        // 요청 답변이 비어있으면 답을 하지 않은 상황이므로 따로 답변 데이터를 추가할 필요가 없다.
        if (!isUpdate && !applicationRequest.getAnswerList().isEmpty()) {
            QuestionType questionType = null;
            RecruitmentFormQuestion matchFormQuestion = null;
            for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
                if (recruitmentFormQuestion.getId().equals(applicationRequest.getQuestionId())) {
                    questionType = recruitmentFormQuestion.getType();
                    matchFormQuestion = recruitmentFormQuestion;
                    break;
                }
            }
            if (questionType == null) {
                throw new ServiceException(ErrorCode.QUESTION_NOT_FOUND);
            } else {
                if (questionType == QuestionType.DESCRIPTION) {
                    recruitmentApplicantDescriptiveAnswerRepository.save(
                            RecruitmentApplicantDescriptiveAnswer.from(applicationRequest.getAnswerList().get(0), matchFormQuestion, applicant));
                    isUpdate = true;
                }
            }
        }
        return isUpdate;
    }

    public boolean updateOptionalAnswer(RecruitmentApplicant applicant, RecruitmentBoard recruitmentBoard,
                                        List<RecruitmentApplicantOptionalAnswer> recruitmentApplicantOptionalAnswerList,
                                        RecruitmentApplicationRequest.RecruitmentApplicantApplicationRequest applicationRequest,
                                        List<Long> toDeleteOptionalAnswerIdList) {

        boolean isUpdate = false;
        Iterator<RecruitmentApplicantOptionalAnswer> optionalAnswerIterator = recruitmentApplicantOptionalAnswerList.iterator();
        List<Integer> checkNumberList = new ArrayList<>();
        // 신청자가 이전에 답변한 선택형 답변 리스트 순차 탐색
        while (optionalAnswerIterator.hasNext()) {
            RecruitmentApplicantOptionalAnswer applicantAnswer = optionalAnswerIterator.next();

            // 요청에서 수정한 답변이 이전에 답변한 적 있는 질문의 답변인 경우 이전 질문의 답변 하나가 요청 답변들 중 존재하는지 체크
            // 존재하면 리스트에 넣고, 존재하지 않으면 이전 질문의 답변을 삭제한다.
            if (applicantAnswer.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getId().equals(applicationRequest.getQuestionId())) {
                boolean shouldDelete = true;
                for (RecruitmentApplicationRequest.RecruitmentApplicantAnswerInfoRequest answerInfoRequest : applicationRequest.getAnswerList()) {
                    if (answerInfoRequest.getNumber().equals(applicantAnswer.getRecruitmentFormAnswer().getNumber())) {
                        shouldDelete = false;
                        checkNumberList.add(answerInfoRequest.getNumber());
                        break;
                    }
                }
                if (shouldDelete) {
                    toDeleteOptionalAnswerIdList.add(applicantAnswer.getId());
                    optionalAnswerIterator.remove();
                }
            }
        }

        // 이전 질문의 답변들과 요청 질문의 답변들의 일치 수가 요청 질문의 답변 수와 다르면
        // 질문 타입이 선택형인지 확인 후 선택형이면 일치하지 않은 요청 답변을 추가한다.
        if (checkNumberList.size() != applicationRequest.getAnswerList().size()) {
            QuestionType questionType = null;
            for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
                if (recruitmentFormQuestion.getId().equals(applicationRequest.getQuestionId())) {
                    questionType = recruitmentFormQuestion.getType();
                    break;
                }
            }

            if (questionType == null) {
                throw new ServiceException(ErrorCode.QUESTION_NOT_FOUND);
            } else {
                if (questionType != QuestionType.DESCRIPTION) {
                    List<RecruitmentApplicantOptionalAnswer> addApplicantOptionalAnswer = new ArrayList<>();
                    for (RecruitmentApplicationRequest.RecruitmentApplicantAnswerInfoRequest answerInfoRequest : applicationRequest.getAnswerList()) {
                        boolean isNumberMatch = false;
                        for (Integer i : checkNumberList) {
                            if (answerInfoRequest.getNumber().equals(i)) {
                                isNumberMatch = true;
                            }
                        }

                        if (!isNumberMatch) {
                            RecruitmentFormAnswer recruitmentFormAnswer = recruitmentFormAnswerRepository
                                    .findByRecruitmentFormQuestion_IdAndNumber(applicationRequest.getQuestionId(), answerInfoRequest.getNumber())
                                    .orElseThrow(() -> new ServiceException(ErrorCode.ANSWER_NOT_FOUND));
                            addApplicantOptionalAnswer.add(RecruitmentApplicantOptionalAnswer.from(recruitmentFormAnswer, applicant));
                        }
                    }
                    recruitmentApplicantOptionalAnswerRepository.saveAll(addApplicantOptionalAnswer);
                    isUpdate = true;
                }
            }
        } else {
            isUpdate = true;
        }
        return isUpdate;
    }

    @Transactional
    public void deleteApplication(Long userId, Long applicantId) {
        RecruitmentApplicant applicant = recruitmentApplicantRepository.findById(applicantId).orElseThrow(() -> new ServiceException(ErrorCode.RECRUITMENT_APPLICANT_NOT_FOUND));
        if (!userId.equals(applicant.getUser().getId())) {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
        recruitmentBoardService.validateDeadLine(userId, applicant.getRecruitmentBoard().toBoardInfoDomain());

        recruitmentApplicantRepository.deleteById(applicantId);
    }

    @Transactional(readOnly = true)
    public MyRecruitmentApplicationPageResponse getUserApplicationList(Long userId, Pageable pageable, RecruitmentBoardType boardType) {
        userService.validateUser(userId);
        Page<RecruitmentApplicant> applicantPage = recruitmentApplicantRepository.findByUser_IdFetchRecruitmentBoard(userId, pageable, boardType);

        return MyRecruitmentApplicationPageResponse.from(applicantPage);
    }


    // 필수 질문 답변 여부 확인
    public void validateEssential(RecruitmentBoard recruitmentBoard, RecruitmentApplicationRequest request) {
        List<Long> essentialQuestionIdList = new ArrayList<>();
        for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
            if (recruitmentFormQuestion.getIsEssential()) {
                essentialQuestionIdList.add(recruitmentFormQuestion.getId());
            }
        }

        for (RecruitmentApplicationRequest.RecruitmentApplicantApplicationRequest applicationRequest : request.getApplication()) {
            for (Long essentialQuestionId : essentialQuestionIdList) {
                if (applicationRequest.getQuestionId().equals(essentialQuestionId)) {
                    if (applicationRequest.getAnswerList() == null || applicationRequest.getAnswerList().isEmpty()) {
                        throw new ServiceException(ErrorCode.OMIT_ESSENTIAL_QUESTION);
                    }
                }
            }
        }
    }

    public void transformToEntityFactory(
            RecruitmentApplicationRequest request,
            RecruitmentBoard recruitmentBoard,
            RecruitmentApplicant applicant,
            List<RecruitmentApplicantDescriptiveAnswer> applicantDescriptiveAnswerList,
            List<RecruitmentApplicantOptionalAnswer> applicantOptionalAnswerList) {

        // 요청에 있는 질문 id를 통해 서술형인지 선택형인지 구분
        request.getApplication()
                .forEach(applicationRequest -> {
                    for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentBoard.getRecruitmentFormQuestionList()) {
                        if (recruitmentFormQuestion.getId().equals(applicationRequest.getQuestionId())) {
                            if (recruitmentFormQuestion.getType() == QuestionType.DESCRIPTION) {
                                applicationRequest.getAnswerList()
                                        .forEach(answerInfoRequest -> {
                                            applicantDescriptiveAnswerList.add(
                                                    RecruitmentApplicantDescriptiveAnswer.from(answerInfoRequest, recruitmentFormQuestion, applicant));
                                        });
                            } else {
                                applicationRequest.getAnswerList()
                                        .forEach(answerInfoRequest -> {
                                            RecruitmentFormAnswer recruitmentFormAnswer = recruitmentFormAnswerRepository.findByRecruitmentFormQuestion_IdAndNumber(applicationRequest.getQuestionId(), answerInfoRequest.getNumber())
                                                    .orElseThrow(() -> new ServiceException(ErrorCode.ANSWER_NOT_FOUND));
                                            applicantOptionalAnswerList.add(
                                                    RecruitmentApplicantOptionalAnswer.from(recruitmentFormAnswer, applicant)
                                            );
                                        });
                            }
                            break;
                        }
                    }
                });
    }
}
