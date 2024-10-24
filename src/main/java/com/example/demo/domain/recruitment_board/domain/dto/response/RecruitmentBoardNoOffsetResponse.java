package com.example.demo.domain.recruitment_board.domain.dto.response;

import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardTag;
import com.example.demo.domain.recruitment_board.domain.dto.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentBoardNoOffsetResponse {
    private boolean nextPage;
    private int pageSize;
    private List<RecruitmentBoardSummaryInfo> boardInfo;

    public static RecruitmentBoardNoOffsetResponse newEmptyListInstance(int pageSize) {
        return RecruitmentBoardNoOffsetResponse.builder()
                .nextPage(false)
                .pageSize(pageSize)
                .boardInfo(new ArrayList<>())
                .build();
    }

    public static RecruitmentBoardNoOffsetResponse from(int pageSize, List<RecruitmentBoard> recruitmentBoardList) {
        boolean nextPage = false;
        if (recruitmentBoardList.size() > pageSize) {
            nextPage = true;
            recruitmentBoardList.remove(pageSize);
        }
        return RecruitmentBoardNoOffsetResponse.builder()
                .nextPage(nextPage)
                .pageSize(pageSize)
                .boardInfo(recruitmentBoardList.stream()
                        .map(RecruitmentBoardSummaryInfo::from)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class RecruitmentBoardSummaryInfo {

        private Long boardId;

        private String title;

        private String summary;

        private RecruitmentBoardType type;

        private RecruitmentBoardTag tag;

        private String recruitmentTarget;

        private String recruitmentNum;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime recruitmentStart;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime recruitmentDeadline;

        public static RecruitmentBoardSummaryInfo from(RecruitmentBoard recruitmentBoardEntity) {
            return RecruitmentBoardSummaryInfo.builder()
                    .boardId(recruitmentBoardEntity.getId())
                    .title(recruitmentBoardEntity.getTitle())
                    .summary(recruitmentBoardEntity.getSummary())
                    .type(recruitmentBoardEntity.getType())
                    .tag(recruitmentBoardEntity.getTag())
                    .recruitmentTarget(recruitmentBoardEntity.getRecruitmentTarget())
                    .recruitmentNum(recruitmentBoardEntity.getRecruitmentNum())
                    .recruitmentStart(recruitmentBoardEntity.getActivityStart())
                    .recruitmentDeadline(recruitmentBoardEntity.getRecruitmentDeadline())
                    .build();
        }
    }
}

