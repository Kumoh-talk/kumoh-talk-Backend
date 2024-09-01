package com.example.demo.domain.seminar_application.domain.dto.response;

import com.example.demo.domain.seminar_application.domain.SeminarApplication;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SeminarApplicationInfo(
        Long id,
        String name,
        String department,
        int grade,
        String studentId,
        String phoneNumber,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate preferredDate,
        String presentationTopic,
        String seminarName,
        String estimatedDuration,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt
) {
    public static SeminarApplicationInfo from(SeminarApplication application) {
        return new SeminarApplicationInfo(
                application.getId(),
                application.getName(),
                application.getDepartment(),
                application.getGrade(),
                application.getStudentId(),
                application.getPhoneNumber(),
                application.getPreferredDate(),
                application.getPresentationTopic(),
                application.getSeminarName(),
                application.getEstimatedDuration(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
