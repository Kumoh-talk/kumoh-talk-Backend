package com.example.demo.domain.study_project_board.domain.entity;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicant;
import com.example.demo.domain.study_project_application.domain.entity.StudyProjectApplicantAnswer;
import com.example.demo.domain.study_project_application.repository.StudyProjectApplicantAnswerRepository;
import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectBoardInfoAndFormRequest;
import com.example.demo.domain.study_project_board.domain.dto.request.StudyProjectFormQuestionRequest;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardTag;
import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardType;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormChoiceAnswerRepository;
import com.example.demo.domain.study_project_board.repository.StudyProjectFormQuestionRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_project_boards")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE study_project_boards SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class StudyProjectBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(length = 100)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(value = EnumType.STRING)
    private StudyProjectBoardType type;

    @Enumerated(value = EnumType.STRING)
    private StudyProjectBoardTag tag;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(length = 50)
    private String recruitmentTarget;

    private String recruitmentNum;

    private LocalDateTime recruitmentDeadline;

    private LocalDateTime activityStart;

    private LocalDateTime activityFinish;

    @Column(length = 50)
    private String activityCycle;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "studyProjectBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyProjectFormQuestion> studyProjectFormQuestionList = new ArrayList<>();

    @OneToMany(mappedBy = "studyProjectBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "studyProjectBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyProjectApplicant> applicantList = new ArrayList<>();

    public void updateStudyProjectFormQuestionList(List<StudyProjectFormQuestion> studyProjectFormQuestionList) {
        this.studyProjectFormQuestionList = studyProjectFormQuestionList;
    }

    @Builder
    public StudyProjectBoard(String title, String summary, String content, StudyProjectBoardType type, StudyProjectBoardTag tag, Status status,
                             String recruitmentTarget, String recruitmentNum, LocalDateTime recruitmentDeadline, LocalDateTime activityStart, LocalDateTime activityFinish,
                             String activityCycle, User user) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.type = type;
        this.tag = tag;
        this.status = status;
        this.recruitmentTarget = recruitmentTarget;
        this.recruitmentNum = recruitmentNum;
        this.recruitmentDeadline = recruitmentDeadline;
        this.activityStart = activityStart;
        this.activityFinish = activityFinish;
        this.activityCycle = activityCycle;
        this.user = user;
    }

    public static StudyProjectBoard from(StudyProjectBoardInfoAndFormRequest request, User user, Status status) {
        StudyProjectBoard studyProjectBoard = StudyProjectBoard.builder()
                .title(request.getBoard().getTitle())
                .summary(request.getBoard().getSummary())
                .content(request.getBoard().getContent())
                .type(request.getBoard().getType())
                .tag(request.getBoard().getTag())
                .status(status)
                .recruitmentTarget(request.getBoard().getRecruitmentTarget())
                .recruitmentNum(request.getBoard().getRecruitmentNum())
                .recruitmentDeadline(request.getBoard().getRecruitmentDeadline())
                .activityStart(request.getBoard().getActivityStart())
                .activityFinish(request.getBoard().getActivityFinish())
                .activityCycle(request.getBoard().getActivityCycle())
                .user(user)
                .build();

        if (request.getForm() != null) {
            for (StudyProjectFormQuestionRequest questionRequest : request.getForm()) {
                StudyProjectFormQuestion question =
                        StudyProjectFormQuestion.from(questionRequest, studyProjectBoard);
                studyProjectBoard.getStudyProjectFormQuestionList().add(question);
            }
        }
        return studyProjectBoard;
    }

    public void updateFromRequest(StudyProjectBoardInfoAndFormRequest request,
                                  Status status,
                                  StudyProjectFormQuestionRepository studyProjectFormQuestionRepository,
                                  StudyProjectFormChoiceAnswerRepository studyProjectFormChoiceAnswerRepository,
                                  StudyProjectApplicantAnswerRepository studyProjectApplicantAnswerRepository) {
        // 게시물 업데이트
        this.title = request.getBoard().getTitle();
        this.summary = request.getBoard().getSummary();
        this.content = request.getBoard().getContent();
        this.type = request.getBoard().getType();
        this.tag = request.getBoard().getTag();
        this.status = status;
        this.recruitmentTarget = request.getBoard().getRecruitmentTarget();
        this.recruitmentNum = request.getBoard().getRecruitmentNum();
        this.recruitmentDeadline = request.getBoard().getRecruitmentDeadline();
        this.activityStart = request.getBoard().getActivityStart();
        this.activityFinish = request.getBoard().getActivityFinish();
        this.activityCycle = request.getBoard().getActivityCycle();

        // 폼 업데이트
        // 수정 질문 수 == 기존 질문 수 -> 단순 update
        // 수정 질문 수 < 기존 질문 수 -> 넘치는 기존 질문들 삭제
        int questionIdx = 0;
        List<StudyProjectFormQuestionRequest> requestForm =
                request.getForm() == null ? new ArrayList<>() : request.getForm();
        for (StudyProjectFormQuestion studyProjectFormQuestion : studyProjectFormQuestionList) {
            try {
                studyProjectFormQuestion.updateFromRequest(requestForm.get(questionIdx), studyProjectFormChoiceAnswerRepository);
                questionIdx++;
            } catch (IndexOutOfBoundsException e) {
                List<Long> deleteIdList = new ArrayList<>();
                for (int i = studyProjectFormQuestionList.size() - 1; i >= questionIdx; i--) {
                    StudyProjectFormQuestion delete = studyProjectFormQuestionList.remove(i);
                    deleteIdList.add(delete.getId());
                }
                // TODO : 질문이 삭제되면, 신청자의 답변도 hard delete 될까?
                studyProjectFormQuestionRepository.hardDeleteQuestionsByIds(deleteIdList);
                return;
            }
        }
        // 수정 질문 수 > 기존 질문 수 -> 넘치는 수정 질문들 추가
        int size = request.getForm() != null ? request.getForm().size() : 0;
        while (questionIdx < size) {
            StudyProjectFormQuestion studyProjectFormQuestion = StudyProjectFormQuestion.from(request.getForm().get(questionIdx++), this);
            studyProjectFormQuestionList.add(studyProjectFormQuestion);
            for (StudyProjectApplicant studyProjectApplicant : applicantList) {
                studyProjectApplicantAnswerRepository.save(StudyProjectApplicantAnswer.newEmptyAnswerInstance(studyProjectFormQuestion, studyProjectApplicant));
            }
        }
    }
}
