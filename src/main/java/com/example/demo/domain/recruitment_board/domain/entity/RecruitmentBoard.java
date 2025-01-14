package com.example.demo.domain.recruitment_board.domain.entity;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.comment.domain.entity.RecruitmentBoardComment;
import com.example.demo.domain.recruitment_application.domain.entity.RecruitmentApplicant;
import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentBoardInfoAndFormRequest;
import com.example.demo.domain.recruitment_board.domain.dto.request.RecruitmentFormQuestionRequest;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormAnswerRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentFormQuestionRepository;
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
@Table(name = "recruitment_boards")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_boards SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentBoard extends BaseEntity implements GenericBoard {
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
    private List<RecruitmentApplicant> applicantList = new ArrayList<>();

    public void updateRecruitmentBoardFormQuestionList(List<RecruitmentFormQuestion> recruitmentFormQuestionList) {
        this.recruitmentFormQuestionList = recruitmentFormQuestionList;
    }

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

    public static RecruitmentBoard from(RecruitmentBoardInfoAndFormRequest request, User user, Status status) {
        RecruitmentBoard recruitmentBoard = RecruitmentBoard.builder()
                .title(request.getBoard().getTitle())
                .summary(request.getBoard().getSummary())
                .host(request.getBoard().getHost())
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
            for (RecruitmentFormQuestionRequest questionRequest : request.getForm()) {
                RecruitmentFormQuestion question =
                        RecruitmentFormQuestion.from(questionRequest, recruitmentBoard);
                recruitmentBoard.getRecruitmentFormQuestionList().add(question);
            }
        }
        return recruitmentBoard;
    }

    public boolean updateFromRequest(RecruitmentBoardInfoAndFormRequest request,
                                     Status status,
                                     RecruitmentFormQuestionRepository recruitmentFormQuestionRepository,
                                     RecruitmentFormAnswerRepository recruitmentFormAnswerRepository) {
        boolean isPublish = false;
        if (this.status == Status.DRAFT && status == Status.PUBLISHED)
            isPublish = true;

        // 게시물 업데이트
        this.title = request.getBoard().getTitle();
        this.summary = request.getBoard().getSummary();
        this.host = request.getBoard().getHost();
        this.content = request.getBoard().getContent();
        this.type = request.getBoard().getType();
        this.tag = request.getBoard().getTag();
        this.status = status;
        this.recruitmentTarget = request.getBoard().getRecruitmentTarget();
        this.recruitmentNum = request.getBoard().getRecruitmentNum();
        this.currentMemberNum = request.getBoard().getCurrentMemberNum();
        this.recruitmentDeadline = request.getBoard().getRecruitmentDeadline();
        this.activityStart = request.getBoard().getActivityStart();
        this.activityFinish = request.getBoard().getActivityFinish();
        this.activityCycle = request.getBoard().getActivityCycle();

        // 폼 업데이트
        // 수정 질문 수 == 기존 질문 수 -> 단순 update
        // 수정 질문 수 < 기존 질문 수 -> 넘치는 기존 질문들 삭제
        int questionIdx = 0;
        List<RecruitmentFormQuestionRequest> requestForm =
                request.getForm() == null ? new ArrayList<>() : request.getForm();
        for (RecruitmentFormQuestion recruitmentFormQuestion : recruitmentFormQuestionList) {
            try {
                recruitmentFormQuestion.updateFromRequest(requestForm.get(questionIdx), recruitmentFormAnswerRepository);
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
                recruitmentFormAnswerRepository.hardDeleteAnswersByIds(deleteAnswerIdList);
                recruitmentFormQuestionRepository.hardDeleteQuestionsByIds(deleteQuestionIdList);
                return isPublish;
            }
        }
        // 수정 질문 수 > 기존 질문 수 -> 넘치는 수정 질문들 추가
        int size = request.getForm() != null ? request.getForm().size() : 0;
        while (questionIdx < size) {
            // 추가된 질문의 id를 알기 위한 저장 쿼리
            RecruitmentFormQuestion savedRecruitmentFormQuestion = recruitmentFormQuestionRepository
                    .save(RecruitmentFormQuestion.from(request.getForm().get(questionIdx++), this));
            recruitmentFormQuestionList.add(savedRecruitmentFormQuestion);
        }

        return isPublish;
    }
}
