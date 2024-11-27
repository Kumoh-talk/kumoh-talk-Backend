package com.example.demo.domain.user_addtional_info.domain.dto.response;

import com.example.demo.domain.user_addtional_info.domain.UserAdditionalInfo;
import com.example.demo.domain.user_addtional_info.domain.vo.StudentStatus;
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
