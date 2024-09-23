package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitmentBoardInfoResponse {
    private Long boardId;

    private String writerNickname;

    private String writerPhoneNumber;

    private String title;

    private String summary;

    private String content;

    private RecruitmentBoardType type;

    private RecruitmentBoardTag tag;

    private Status status;

    private String recruitmentTarget;

    private String recruitmentNum;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime recruitmentStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime recruitmentDeadline;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activityStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime activityFinish;

    private String activityCycle;

    public static RecruitmentBoardInfoResponse from(RecruitmentBoard recruitmentBoardEntity) {
        return RecruitmentBoardInfoResponse.builder()
                .boardId(recruitmentBoardEntity.getId())
                .writerNickname(recruitmentBoardEntity.getUser().getNickname())
                .writerPhoneNumber(recruitmentBoardEntity.getUser().getUserAdditionalInfo().getPhoneNumber())
                .title(recruitmentBoardEntity.getTitle())
                .summary(recruitmentBoardEntity.getSummary())
                .content(recruitmentBoardEntity.getContent())
                .type(recruitmentBoardEntity.getType())
                .tag(recruitmentBoardEntity.getTag())
                .status(recruitmentBoardEntity.getStatus())
                .recruitmentTarget(recruitmentBoardEntity.getRecruitmentTarget())
                .recruitmentNum(recruitmentBoardEntity.getRecruitmentNum())
                .recruitmentStart(recruitmentBoardEntity.getActivityStart())
                .recruitmentDeadline(recruitmentBoardEntity.getRecruitmentDeadline())
                .activityStart(recruitmentBoardEntity.getActivityStart())
                .activityFinish(recruitmentBoardEntity.getActivityFinish())
                .activityCycle(recruitmentBoardEntity.getActivityCycle())
                .build();
    }
}
