package com.example.demo.application.user_additional_info.dto.response;

import com.example.demo.application.user_additional_info.dto.vo.StudentStatus;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserAdditionalInfoResponse(
        String email,
        String department,
        int studentId,
        int grade,
        StudentStatus studentStatus,
        String phoneNumber,
        boolean isUpdated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt
) {
    public static UserAdditionalInfoResponse from(UserAdditionalInfo userAdditionalInfo) {
        return new UserAdditionalInfoResponse(
                userAdditionalInfo.getEmail(),
                userAdditionalInfo.getDepartment(),
                userAdditionalInfo.getStudentId(),
                userAdditionalInfo.getGrade(),
                userAdditionalInfo.getStudentStatus(),
                userAdditionalInfo.getPhoneNumber(),
                userAdditionalInfo.isUpdated(),
                userAdditionalInfo.getCreatedAt(),
                userAdditionalInfo.getUpdatedAt()
        );
    }
}
