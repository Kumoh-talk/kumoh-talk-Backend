package com.example.demo.domain.seminar_application.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SeminarApplicationRequest(
        @NotBlank
        String name,
        @NotBlank
        String department,
        @NotNull
        int grade,
        @NotBlank
        String studentId,
        @NotBlank
        String phoneNumber,
        @NotNull
        LocalDate preferredDate,
        @NotBlank
        String presentationTopic,
        @NotBlank
        String seminarName,
        @NotBlank
        String estimatedDuration
) {
}
