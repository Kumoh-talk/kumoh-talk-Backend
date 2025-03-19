package com.example.demo.domain.seminar_application.entity;

import com.example.demo.domain.user.entity.UserTarget;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SeminarApplicationInfo {
    private Long id;
    private UserTarget userTarget;
    private SeminarApplicant seminarApplicant;
    private SeminarContent seminarContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public SeminarApplicationInfo(Long id, UserTarget userTarget, SeminarApplicant seminarApplicant, SeminarContent seminarContent, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userTarget = userTarget;
        this.seminarApplicant = seminarApplicant;
        this.seminarContent = seminarContent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
