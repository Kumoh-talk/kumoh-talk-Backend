package com.example.demo.domain.seminar_application.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SeminarApplicant {
    private String name;
    private String department;
    private int grade;
    private String studentId;
    private String phoneNumber;

    @Builder
    public SeminarApplicant(String name, String department, int grade, String studentId, String phoneNumber) {
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
    }
}
