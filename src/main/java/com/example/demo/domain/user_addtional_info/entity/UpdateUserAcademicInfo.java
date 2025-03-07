package com.example.demo.domain.user_addtional_info.entity;

import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
public class UpdateUserAcademicInfo {
    private final int grade;
    private final StudentStatus studentStatus;

    @Builder
    public UpdateUserAcademicInfo(int grade, StudentStatus studentStatus) {
        this.grade = grade;
        this.studentStatus = studentStatus;
    }
}
