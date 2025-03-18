package com.example.demo.domain.user_addtional_info.entity;

import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserAdditionalInfoData {
    private final Long id;
    private final String email;
    private final String department; // 정해져있음.
    private final int studentId;
    private final int grade;
    private final StudentStatus studentStatus;
    private final String phoneNumber;
    private final boolean isUpdated;
    private final LocalDateTime updatedAt;
    private final LocalDateTime createdAt;

    @Builder
    public UserAdditionalInfoData(Long id, String email, String department, int studentId, int grade, StudentStatus studentStatus, String phoneNumber, boolean isUpdated, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.department = department;
        this.studentId = studentId;
        this.grade = grade;
        this.studentStatus = studentStatus;
        this.phoneNumber = phoneNumber;
        this.isUpdated = isUpdated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
