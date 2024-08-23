package com.example.demo.domain.user_addtional_info.domain.dto.request;

import com.example.demo.domain.user_addtional_info.domain.vo.StudentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserAdditionalInfoRequest(
        @NotBlank String email,
        @NotBlank String department,
        @NotNull int studentId,
        @NotNull int grade,
        @NotNull StudentStatus studentStatus,
        @NotBlank String phoneNumber
) {
}
