package com.example.demo.application.seminar_application.dto.response;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema
public record SeminarApplicationResponse (
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
    public static SeminarApplicationResponse from(SeminarApplicationInfo application) {
        return new SeminarApplicationResponse(
                application.getId(),
                application.getSeminarApplicant().getName(),
                application.getSeminarApplicant().getDepartment(),
                application.getSeminarApplicant().getGrade(),
                application.getSeminarApplicant().getStudentId(),
                application.getSeminarApplicant().getPhoneNumber(),
                application.getSeminarContent().getPreferredDate(),
                application.getSeminarContent().getPresentationTopic(),
                application.getSeminarContent().getSeminarName(),
                application.getSeminarContent().getEstimatedDuration(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
