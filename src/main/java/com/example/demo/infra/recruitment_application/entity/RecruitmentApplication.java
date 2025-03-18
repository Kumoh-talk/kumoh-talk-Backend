package com.example.demo.infra.recruitment_application.entity;

import com.example.demo.domain.recruitment_application.entity.MyRecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationAnswerInfo;
import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.infra.user.entity.User;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.entity.RecruitmentFormQuestion;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "recruitment_applications")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE recruitment_applications SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class RecruitmentApplication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_board_id", nullable = false)
    private RecruitmentBoard recruitmentBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recruitmentApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecruitmentApplicationDescriptiveAnswer> descriptiveAnswerList = new ArrayList<>();

    @OneToMany(mappedBy = "recruitmentApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecruitmentApplicationOptionalAnswer> optionalAnswerList = new ArrayList<>();

    @Builder
    public RecruitmentApplication(RecruitmentBoard recruitmentBoard, User user) {
        this.recruitmentBoard = recruitmentBoard;
        this.user = user;
    }

    public static RecruitmentApplication of(User user, RecruitmentBoard recruitmentBoard) {
        return RecruitmentApplication.builder()
                .recruitmentBoard(recruitmentBoard)
                .user(user)
                .build();
    }

    public RecruitmentApplicationInfo toApplicantDomainWIthBoard() {
        return RecruitmentApplicationInfo.builder()
                .applicationId(id)
                .userId(user.getId())
                .recruitmentBoardId(recruitmentBoard.getId())
                .answerInfoList(null)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

    public RecruitmentApplicationInfo toApplicantDomain() {
        return RecruitmentApplicationInfo.builder()
                .applicationId(id)
                .userId(user.getId())
                .answerInfoList(null)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

    public RecruitmentApplicationInfo toApplicationDomain(
            List<RecruitmentFormQuestion> recruitmentFormQuestionList,
            List<RecruitmentApplicationDescriptiveAnswer> descriptiveAnswerList,
            List<RecruitmentApplicationOptionalAnswer> optionalAnswerList) {

        RecruitmentApplicationInfo recruitmentApplicationInfo = RecruitmentApplicationInfo.builder()
                .applicationId(id)
                .userId(user.getId())
                .answerInfoList(new ArrayList<>())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();

        mergeAnswerList(recruitmentFormQuestionList, recruitmentApplicationInfo, descriptiveAnswerList, optionalAnswerList);
        recruitmentApplicationInfo.getAnswerInfoList().sort(Comparator.comparingInt(RecruitmentApplicationAnswerInfo::getQuestionNumber));

        return recruitmentApplicationInfo;
    }

    public MyRecruitmentApplicationInfo toMyApplicationDomain() {
        return MyRecruitmentApplicationInfo.builder()
                .applicationId(id)
                .userId(user.getId())
                .recruitmentBoardId(recruitmentBoard.getId())
                .boardTitle(recruitmentBoard.getTitle())
                .boardType(recruitmentBoard.getType())
                .applicationCreatedAt(getCreatedAt())
                .applicationUpdatedAt(getUpdatedAt())
                .boardCreatedAt(recruitmentBoard.getCreatedAt())
                .boardUpdatedAt(recruitmentBoard.getUpdatedAt())
                .build();
    }

    private void mergeAnswerList(
            List<RecruitmentFormQuestion> recruitmentFormQuestionList,
            RecruitmentApplicationInfo recruitmentApplicationInfo,
            List<RecruitmentApplicationDescriptiveAnswer> descriptiveAnswerList,
            List<RecruitmentApplicationOptionalAnswer> optionalAnswerList) {
        Map<Long, RecruitmentApplicationAnswerInfo> applicaitonAnswerInfoMap = recruitmentFormQuestionList.stream()
                .collect(Collectors.toMap(RecruitmentFormQuestion::getId, RecruitmentFormQuestion::toApplicationDomain));

        descriptiveAnswerList.forEach(answer -> {
            RecruitmentApplicationAnswerInfo applicationAnswerInfo = applicaitonAnswerInfoMap.get(answer.getRecruitmentFormQuestion().getId());
            if (applicationAnswerInfo != null) {
                applicationAnswerInfo.getAnswerInfoList().add(answer.toDomain());
            }
        });
        optionalAnswerList.forEach(answer -> {
            RecruitmentApplicationAnswerInfo applicationAnswerInfo = applicaitonAnswerInfoMap.get(answer.getRecruitmentFormAnswer().getRecruitmentFormQuestion().getId());
            if (applicationAnswerInfo != null) {
                applicationAnswerInfo.getAnswerInfoList().add(answer.toDomain());
            }
        });

        applicaitonAnswerInfoMap.values()
                .forEach((applicationAnswerInfo) ->
                        applicationAnswerInfo.getAnswerInfoList()
                                .sort(Comparator.comparingInt(RecruitmentApplicationAnswerInfo.AnswerInfo::getAnswerNumber)));

        recruitmentApplicationInfo.getAnswerInfoList().addAll(applicaitonAnswerInfoMap.values());
    }
}
