package com.example.demo.domain.study_project_board.domain.entity;

import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectFormChoiceAnswerRequest;
import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectFormQuestionRequest;
import com.example.demo.domain.study_project_board.domain.dto.vo.QuestionType;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormChoiceAnswerRepository;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "study_project_form_questions")
@NoArgsConstructor
@Getter
@SQLRestriction(value = "deleted_at is NULL")
public class StudyProjectFormQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(nullable = false)
    private String question;

    @Enumerated(value = EnumType.STRING)
    private QuestionType type;

    private Boolean isEssential;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_board_id", nullable = false)
    private StudyProjectBoard studyProjectBoard;

    @Setter
    @OneToMany(mappedBy = "studyProjectFormQuestion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("number ASC")
    private List<StudyProjectFormChoiceAnswer> studyProjectFormChoiceAnswerList = new ArrayList<>();

    @Builder
    public StudyProjectFormQuestion(int number, String question, QuestionType type, Boolean isEssential, StudyProjectBoard studyProjectBoard, List<StudyProjectFormChoiceAnswer> studyProjectFormChoiceAnswerList) {
        this.number = number;
        this.question = question;
        this.type = type;
        this.isEssential = isEssential;
        this.studyProjectBoard = studyProjectBoard;
        this.studyProjectFormChoiceAnswerList = studyProjectFormChoiceAnswerList;
    }

    public static StudyProjectFormQuestion from(
            StudyProjectFormQuestionRequest questionRequest,
            StudyProjectBoard studyProjectBoard) {
        StudyProjectFormQuestion studyProjectFormQuestion = StudyProjectFormQuestion.builder()
                .number(questionRequest.getNumber())
                .question(questionRequest.getQuestion())
                .type(questionRequest.getType())
                .isEssential(questionRequest.getIsEssential())
                .studyProjectBoard(studyProjectBoard)
                .build();

        List<StudyProjectFormChoiceAnswerRequest> requestAnswerList =
                questionRequest.getAnswerList() == null ? new ArrayList<>() : questionRequest.getAnswerList();

        studyProjectFormQuestion.setStudyProjectFormChoiceAnswerList(requestAnswerList.stream()
                .map(v -> StudyProjectFormChoiceAnswer.from(v, studyProjectFormQuestion))
                .collect(Collectors.toList()));
        return studyProjectFormQuestion;
    }

    public void updateFromRequest(StudyProjectFormQuestionRequest request,
                                  StudyProjectFormChoiceAnswerRepository studyProjectFormChoiceAnswerRepository) {
        // 질문 수정
        this.number = request.getNumber();
        this.question = request.getQuestion();
        this.type = request.getType();
        this.isEssential = request.getIsEssential();

        // 객관식 답 수정
        int answerIdx = 0;
        List<StudyProjectFormChoiceAnswerRequest> requestAnswerList =
                request.getAnswerList() == null ? new ArrayList<>() : request.getAnswerList();
        for (StudyProjectFormChoiceAnswer studyProjectFormChoiceAnswer : studyProjectFormChoiceAnswerList) {
            try {
                studyProjectFormChoiceAnswer.updateFromRequest(requestAnswerList.get(answerIdx));
                answerIdx++;
            } catch (IndexOutOfBoundsException e) {
                List<Long> deleteIdList = new ArrayList<>();
                for (int i = studyProjectFormChoiceAnswerList.size() - 1; i >= answerIdx; i--) {
                    StudyProjectFormChoiceAnswer delete = studyProjectFormChoiceAnswerList.remove(i);
                    deleteIdList.add(delete.getId());
                }
                // TODO : hardDelete는 명시 삭제 X?
                studyProjectFormChoiceAnswerRepository.hardDeleteAnswersByIds(deleteIdList);
                return;
            }
        }
        int size = request.getAnswerList() != null ? request.getAnswerList().size() : 0;
        while (answerIdx < size) {
            studyProjectFormChoiceAnswerList.add(
                    StudyProjectFormChoiceAnswer.from(request.getAnswerList().get(answerIdx++), this));
        }
    }


}
