package com.example.demo.application.user_additional_info.dto.response;

import com.example.demo.domain.user_addtional_info.entity.UserAdditionalInfoData;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
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
    public static UserAdditionalInfoResponse toUserAdditionalInfoResponse(UserAdditionalInfoData userAdditionalInfoData) {
        return new UserAdditionalInfoResponse(
                userAdditionalInfoData.getEmail(),
                userAdditionalInfoData.getDepartment(),
                userAdditionalInfoData.getStudentId(),
                userAdditionalInfoData.getGrade(),
                userAdditionalInfoData.getStudentStatus(),
                userAdditionalInfoData.getPhoneNumber(),
                userAdditionalInfoData.isUpdated(),
                userAdditionalInfoData.getCreatedAt(),
                userAdditionalInfoData.getUpdatedAt()
        );
    }
}
