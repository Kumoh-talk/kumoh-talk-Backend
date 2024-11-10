package com.example.demo.domain.recruitment_board.domain.entity;

import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentFormChoiceAnswerRequest;
import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentFormQuestionRequest;
import com.example.demo.domain.recruitment_board.domain.vo.QuestionType;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormChoiceAnswerRepository;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "recruitment_form_questions")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_form_questions SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentFormQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false, length = 100)
    private String question;

    @Enumerated(value = EnumType.STRING)
    private QuestionType type;

    private Boolean isEssential;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_board_id", nullable = false)
    private RecruitmentBoard recruitmentBoard;

    @Setter
    @OneToMany(mappedBy = "recruitmentFormQuestion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("number ASC")
    private List<RecruitmentFormChoiceAnswer> recruitmentFormChoiceAnswerList = new ArrayList<>();

    @Builder
    public RecruitmentFormQuestion(int number, String question, QuestionType type, Boolean isEssential, RecruitmentBoard recruitmentBoard, List<RecruitmentFormChoiceAnswer> recruitmentFormChoiceAnswerList) {
        this.number = number;
        this.question = question;
        this.type = type;
        this.isEssential = isEssential;
        this.recruitmentBoard = recruitmentBoard;
        this.recruitmentFormChoiceAnswerList = recruitmentFormChoiceAnswerList;
    }

    public static RecruitmentFormQuestion from(
            RecruitmentFormQuestionRequest questionRequest,
            RecruitmentBoard recruitmentBoard) {
        RecruitmentFormQuestion recruitmentFormQuestion = RecruitmentFormQuestion.builder()
                .number(questionRequest.getNumber())
                .question(questionRequest.getQuestion())
                .type(questionRequest.getType())
                .isEssential(questionRequest.getIsEssential())
                .recruitmentBoard(recruitmentBoard)
                .build();

        List<RecruitmentFormChoiceAnswerRequest> requestAnswerList =
                questionRequest.getAnswerList() == null ? new ArrayList<>() : questionRequest.getAnswerList();

        recruitmentFormQuestion.setRecruitmentFormChoiceAnswerList(requestAnswerList.stream()
                .map(v -> RecruitmentFormChoiceAnswer.from(v, recruitmentFormQuestion))
                .collect(Collectors.toList()));
        return recruitmentFormQuestion;
    }

    public void updateFromRequest(RecruitmentFormQuestionRequest request,
                                  RecruitmentFormChoiceAnswerRepository recruitmentFormChoiceAnswerRepository) {
        // 질문 수정
        this.number = request.getNumber();
        this.question = request.getQuestion();
        this.type = request.getType();
        this.isEssential = request.getIsEssential();

        // 객관식 답 수정
        int answerIdx = 0;
        List<RecruitmentFormChoiceAnswerRequest> requestAnswerList =
                request.getAnswerList() == null ? new ArrayList<>() : request.getAnswerList();
        for (RecruitmentFormChoiceAnswer recruitmentFormChoiceAnswer : recruitmentFormChoiceAnswerList) {
            try {
                recruitmentFormChoiceAnswer.updateFromRequest(requestAnswerList.get(answerIdx));
                answerIdx++;
            } catch (IndexOutOfBoundsException e) {
                List<Long> deleteIdList = new ArrayList<>();
                for (int i = recruitmentFormChoiceAnswerList.size() - 1; i >= answerIdx; i--) {
                    RecruitmentFormChoiceAnswer delete = recruitmentFormChoiceAnswerList.remove(i);
                    deleteIdList.add(delete.getId());
                }
                recruitmentFormChoiceAnswerRepository.hardDeleteAnswersByIds(deleteIdList);
                return;
            }
        }
        int size = request.getAnswerList() != null ? request.getAnswerList().size() : 0;
        while (answerIdx < size) {
            // 추가된 객관식 선택지의 id를 알기 위한 저장 쿼리
            RecruitmentFormChoiceAnswer recruitmentFormChoiceAnswer = recruitmentFormChoiceAnswerRepository
                    .save(RecruitmentFormChoiceAnswer.from(request.getAnswerList().get(answerIdx++), this));
            recruitmentFormChoiceAnswerList.add(recruitmentFormChoiceAnswer);
        }
    }
}
