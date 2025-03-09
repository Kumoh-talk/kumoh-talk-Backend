package com.example.demo.domain.recruitment_application.entity;

import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MyRecruitmentApplicationInfo {
    private Long applicationId;
    private Long userId;
    private Long recruitmentBoardId;
    private String boardTitle;
    private RecruitmentBoardType boardType;
    private LocalDateTime applicationCreatedAt;
    private LocalDateTime applicationUpdatedAt;
    private LocalDateTime boardCreatedAt;
    private LocalDateTime boardUpdatedAt;
}
