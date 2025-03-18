package com.example.demo.domain.user.entity;

import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfile {
    private final String name;
    private final String nickname;
    private final String profileImageUrl;
    private final StudentStatus studentStatus;
    private final String department;
    private final int grade;
    private final int studentId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    private UserProfile(String name, String nickname, String profileImageUrl, StudentStatus studentStatus, String department, int grade, int studentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.studentStatus = studentStatus;
        this.department = department;
        this.grade = grade;
        this.studentId = studentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
