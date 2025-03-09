package com.example.demo.infra.recruitment_board.entity;

import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardAndFormInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentBoardInfo;
import com.example.demo.domain.recruitment_board.entity.RecruitmentFormQuestionInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.recruitment_application.entity.RecruitmentApplication;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormAnswerJpaRepository;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentFormQuestionJpaRepository;
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
import java.util.stream.Collectors;

@Entity
@Table(name = "recruitment_boards")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_boards SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentBoard extends BaseEntity implements CommentBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(length = 100)
    private String summary;

    @Column(length = 50)
    private String host;

    @Column(length = 1000)
    private String content;

    @Enumerated(value = EnumType.STRING)
    private RecruitmentBoardType type;

    @Enumerated(value = EnumType.STRING)
    private RecruitmentBoardTag tag;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(length = 50)
    private String recruitmentTarget;

    private Integer recruitmentNum;

    private Integer currentMemberNum;

    private LocalDateTime recruitmentDeadline;

    private LocalDateTime activityStart;

    private LocalDateTime activityFinish;

    @Column(length = 50)
    private String activityCycle;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recruitmentBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("number ASC")
    private List<RecruitmentFormQuestion> recruitmentFormQuestionList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecruitmentBoardComment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "recruitmentBoard", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecruitmentApplication> applicationList = new ArrayList<>();

    @Builder
    public RecruitmentBoard(String title, String summary, String host, String content, RecruitmentBoardType type, RecruitmentBoardTag tag, Status status,
                            String recruitmentTarget, int recruitmentNum, int currentMemberNum, LocalDateTime recruitmentDeadline, LocalDateTime activityStart, LocalDateTime activityFinish,
                            String activityCycle, User user) {
        this.title = title;
        this.summary = summary;
        this.host = host;
        this.content = content;
        this.type = type;
        this.tag = tag;
        this.status = status;
        this.recruitmentTarget = recruitmentTarget;
        this.recruitmentNum = recruitmentNum;
        this.currentMemberNum = currentMemberNum;
        this.recruitmentDeadline = recruitmentDeadline;
        this.activityStart = activityStart;
        this.activityFinish = activityFinish;
        this.activityCycle = activityCycle;
        this.user = user;
    }

    public static RecruitmentBoard from(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo, User user) {
        RecruitmentBoard recruitmentBoard = RecruitmentBoard.builder()
                .title(recruitmentBoardAndFormInfo.getBoard().getTitle())
                .summary(recruitmentBoardAndFormInfo.getBoard().getSummary())
                .host(recruitmentBoardAndFormInfo.getBoard().getHost())
                .content(recruitmentBoardAndFormInfo.getBoard().getContent())
                .type(recruitmentBoardAndFormInfo.getBoard().getType())
                .tag(recruitmentBoardAndFormInfo.getBoard().getTag())
                .status(recruitmentBoardAndFormInfo.getBoard().getStatus())
                .recruitmentTarget(recruitmentBoardAndFormInfo.getBoard().getRecruitmentTarget())
                .recruitmentNum(recruitmentBoardAndFormInfo.getBoard().getRecruitmentNum())
                .currentMemberNum(recruitmentBoardAndFormInfo.getBoard().getCurrentMemberNum())
                .recruitmentDeadline(recruitmentBoardAndFormInfo.getBoard().getRecruitmentDeadline())
                .activityStart(recruitmentBoardAndFormInfo.getBoard().getActivityStart())
                .activityFinish(recruitmentBoardAndFormInfo.getBoard().getActivityFinish())
                .activityCycle(recruitmentBoardAndFormInfo.getBoard().getActivityCycle())
                .user(user)
                .build();

        recruitmentBoardAndFormInfo.getForm().stream().map(question -> RecruitmentFormQuestion.from(question, recruitmentBoard))
                .forEach((question) -> recruitmentBoard.getRecruitmentFormQuestionList().add(question));

        return recruitmentBoard;
    }

    public RecruitmentBoardInfo toBoardInfoDomain() {
        return RecruitmentBoardInfo.builder()
                .boardId(id)
                .userId(user.getId())
                .title(title)
                .summary(summary)
                .host(host)
                .content(content)
                .type(type)
                .tag(tag)
                .status(status)
                .recruitmentTarget(recruitmentTarget)
                .recruitmentNum(recruitmentNum)
                .currentMemberNum(currentMemberNum)
                .recruitmentDeadline(recruitmentDeadline)
                .activityStart(activityStart)
                .activityFinish(activityFinish)
                .activityCycle(activityCycle)
                .createdAt(getCreatedAt())
                .build();
    }

    public RecruitmentBoardAndFormInfo toBoardAndFormInfoDomain() {
        return RecruitmentBoardAndFormInfo.builder()
                .board(toBoardInfoDomain())
                .form(recruitmentFormQuestionList.stream()
                        .map(RecruitmentFormQuestion::toFormDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public void update(RecruitmentBoardAndFormInfo recruitmentBoardAndFormInfo,
                       RecruitmentFormQuestionJpaRepository recruitmentFormQuestionJpaRepository,
                       RecruitmentFormAnswerJpaRepository recruitmentFormAnswerJpaRepository) {
        // 게시물 업데이트
        this.title = recruitmentBoardAndFormInfo.getBoard().getTitle();
        this.summary = recruitmentBoardAndFormInfo.getBoard().getSummary();
        this.host = recruitmentBoardAndFormInfo.getBoard().getHost();
        this.content = recruitmentBoardAndFormInfo.getBoard().getContent();
        this.type = recruitmentBoardAndFormInfo.getBoard().getType();
        this.tag = recruitmentBoardAndFormInfo.getBoard().getTag();
        this.status = recruitmentBoardAndFormInfo.getBoard().getStatus();
        this.recruitmentTarget = recruitmentBoardAndFormInfo.getBoard().getRecruitmentTarget();
        this.recruitmentNum = recruitmentBoardAndFormInfo.getBoard().getRecruitmentNum();
        this.currentMemberNum = recruitmentBoardAndFormInfo.getBoard().getCurrentMemberNum();
        this.recruitmentDeadline = recruitmentBoardAndFormInfo.getBoard().getRecruitmentDeadline();
        this.activityStart = recruitmentBoardAndFormInfo.getBoard().getActivityStart();
        this.activityFinish = recruitmentBoardAndFormInfo.getBoard().getActivityFinish();
        this.activityCycle = recruitmentBoardAndFormInfo.getBoard().getActivityCycle();

        // 수정 질문 수 == 기존 질문 수 -> 단순 update
        // 수정 질문 수 < 기존 질문 수 -> 넘치는 기존 질문들 삭제
        int questionIdx = 0;
        List<RecruitmentFormQuestionInfo> updateForm =
                recruitmentBoardAndFormInfo.getForm() == null ? new ArrayList<>() : recruitmentBoardAndFormInfo.getForm();
        for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentFormQuestionList) {
            try {
                recruitmentFormQuestion.update(updateForm.get(questionIdx), recruitmentFormAnswerJpaRepository);
                questionIdx++;
            } catch (IndexOutOfBoundsException e) {
                List<Long> deleteQuestionIdList = new ArrayList<>();
                List<Long> deleteAnswerIdList = new ArrayList<>();
                for (int i = recruitmentFormQuestionList.size() - 1; i >= questionIdx; i--) {
                    RecruitmentFormQuestion delete = recruitmentFormQuestionList.remove(i);
                    for (RecruitmentFormAnswer recruitmentFormAnswer : delete.getRecruitmentFormAnswerList()) {
                        deleteAnswerIdList.add(recruitmentFormAnswer.getId());
                    }
                    deleteQuestionIdList.add(delete.getId());
                }
                recruitmentFormAnswerJpaRepository.hardDeleteAnswersByIds(deleteAnswerIdList);
                recruitmentFormQuestionJpaRepository.hardDeleteQuestionsByIds(deleteQuestionIdList);
            }
        }
        // 수정 질문 수 > 기존 질문 수 -> 넘치는 수정 질문들 추가
        int size = recruitmentBoardAndFormInfo.getForm() != null ? recruitmentBoardAndFormInfo.getForm().size() : 0;
        while (questionIdx < size) {
            // 추가된 질문의 id를 알기 위한 저장 쿼리
            RecruitmentFormQuestion savedRecruitmentFormQuestion = recruitmentFormQuestionJpaRepository
                    .save(RecruitmentFormQuestion.from(recruitmentBoardAndFormInfo.getForm().get(questionIdx++), this));
            recruitmentFormQuestionList.add(savedRecruitmentFormQuestion);
        }
    }
}
