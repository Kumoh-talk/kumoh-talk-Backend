package com.example.demo.domain.recruitment_application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecruitmentApplicationInfo {
    private Long applicationId;
    private Long userId;
    private Long recruitmentBoardId;
    private List<RecruitmentApplicationAnswerInfo> answerInfoList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
