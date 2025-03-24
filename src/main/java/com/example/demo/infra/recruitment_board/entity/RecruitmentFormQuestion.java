package com.example.demo.infra.recruitment_board.entity;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentFormAnswerInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentFormQuestionInfo;
import com.example.demo.domain.recruitment_board.entity.vo.QuestionType;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormAnswerJpaRepository;
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
    private List<RecruitmentFormAnswer> recruitmentFormAnswerList = new ArrayList<>();

    @Builder
    public RecruitmentFormQuestion(int number, String question, QuestionType type, Boolean isEssential, RecruitmentBoard recruitmentBoard) {
        this.number = number;
        this.question = question;
        this.type = type;
        this.isEssential = isEssential;
        this.recruitmentBoard = recruitmentBoard;
    }

    public RecruitmentFormQuestionInfo toFormDomain() {
        return RecruitmentFormQuestionInfo.builder()
                .questionId(id)
                .number(number)
                .question(question)
                .type(type)
                .isEssential(isEssential)
                .answerList(recruitmentFormAnswerList.stream()
                        .map(RecruitmentFormAnswer::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public RecruitmentApplicationAnswerInfo toApplicationDomain() {
        return RecruitmentApplicationAnswerInfo.builder()
                .questionId(id)
                .questionNumber(number)
                .question(question)
                .answerInfoList(new ArrayList<>())
                .build();
    }

    public static RecruitmentFormQuestion from(RecruitmentFormQuestionInfo recruitmentFormQuestionInfo, RecruitmentBoard recruitmentBoard) {
        RecruitmentFormQuestion recruitmentFormQuestion = RecruitmentFormQuestion.builder()
                .number(recruitmentFormQuestionInfo.getNumber())
                .question(recruitmentFormQuestionInfo.getQuestion())
                .type(recruitmentFormQuestionInfo.getType())
                .isEssential(recruitmentFormQuestionInfo.getIsEssential())
                .recruitmentBoard(recruitmentBoard)
                .build();

        recruitmentFormQuestionInfo.getAnswerList().stream()
                .map(answer -> RecruitmentFormAnswer.from(answer, recruitmentFormQuestion))
                .forEach(answer -> recruitmentFormQuestion.getRecruitmentFormAnswerList().add(answer));

        return recruitmentFormQuestion;
    }

    public void update(RecruitmentFormQuestionInfo recruitmentFormQuestionInfo,
                       RecruitmentFormAnswerJpaRepository recruitmentFormAnswerJpaRepository) {
        // 질문 수정
        this.number = recruitmentFormQuestionInfo.getNumber();
        this.question = recruitmentFormQuestionInfo.getQuestion();
        this.type = recruitmentFormQuestionInfo.getType();
        this.isEssential = recruitmentFormQuestionInfo.getIsEssential();

        // 객관식 답 수정
        int answerIdx = 0;
        List<RecruitmentFormAnswerInfo> requestAnswerList =
                recruitmentFormQuestionInfo.getAnswerList() == null ? new ArrayList<>() : recruitmentFormQuestionInfo.getAnswerList();
        for (RecruitmentFormAnswer recruitmentFormAnswer : recruitmentFormAnswerList) {
            try {
                recruitmentFormAnswer.update(requestAnswerList.get(answerIdx));
                answerIdx++;
            } catch (IndexOutOfBoundsException e) {
                List<Long> deleteIdList = new ArrayList<>();
                for (int i = recruitmentFormAnswerList.size() - 1; i >= answerIdx; i--) {
                    RecruitmentFormAnswer delete = recruitmentFormAnswerList.remove(i);
                    deleteIdList.add(delete.getId());
                }
                recruitmentFormAnswerJpaRepository.hardDeleteAnswersByIds(deleteIdList);
                return;
            }
        }
        int size = recruitmentFormQuestionInfo.getAnswerList() != null ? recruitmentFormQuestionInfo.getAnswerList().size() : 0;
        while (answerIdx < size) {
            // 추가된 객관식 선택지의 id를 알기 위한 저장 쿼리
            RecruitmentFormAnswer recruitmentFormAnswer = recruitmentFormAnswerJpaRepository
                    .save(RecruitmentFormAnswer.from(recruitmentFormQuestionInfo.getAnswerList().get(answerIdx++), this));
            recruitmentFormAnswerList.add(recruitmentFormAnswer);
        }
    }
}
