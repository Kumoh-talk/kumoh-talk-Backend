package com.example.demo.application.user.dto.response;

import com.example.demo.application.user_additional_info.dto.vo.StudentStatus;
import com.example.demo.infra.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserProfile(
        String name,
        String nickname,
        String profileImageUrl,
        UserAdditionalProfile userAdditionalProfile,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt
) {
    @Getter
    @AllArgsConstructor
    public static class UserAdditionalProfile {
        private StudentStatus studentStatus;
        private String department;
        private int grade;
        private int studentId;
    }

    public static UserProfile create(User user) {
        UserAdditionalProfile userAdditionalProfile = null;

        if (user.getUserAdditionalInfo() != null) {
            StudentStatus studentStatus = user.getUserAdditionalInfo().getStudentStatus();
            String department = user.getUserAdditionalInfo().getDepartment();
            int grade = user.getUserAdditionalInfo().getGrade();
            int studentId = user.getUserAdditionalInfo().getStudentId();

            userAdditionalProfile = new UserAdditionalProfile(studentStatus, department, grade, studentId);
        }

        return new UserProfile(
                user.getName(),
                user.getNickname(),
                user.getProfileImageUrl(),
                userAdditionalProfile,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
