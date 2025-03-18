package com.example.demo.application.user.dto.response;

import com.example.demo.domain.user.entity.UserProfile;
import com.example.demo.domain.user_addtional_info.vo.StudentStatus;
import com.example.demo.infra.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserProfileResponse(
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

    public static UserProfileResponse create(User user) {
        UserAdditionalProfile userAdditionalProfile = null;

        if (user.getUserAdditionalInfo() != null) {
            StudentStatus studentStatus = user.getUserAdditionalInfo().getStudentStatus();
            String department = user.getUserAdditionalInfo().getDepartment();
            int grade = user.getUserAdditionalInfo().getGrade();
            int studentId = user.getUserAdditionalInfo().getStudentId();

            userAdditionalProfile = new UserAdditionalProfile(studentStatus, department, grade, studentId);
        }

        return new UserProfileResponse(
                user.getName(),
                user.getNickname(),
                user.getProfileImageUrl(),
                userAdditionalProfile,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static UserProfileResponse toUserProfileResponse(UserProfile userProfile) {
        UserAdditionalProfile userAdditionalProfile = null;

        if(userProfile.getStudentStatus() != null && userProfile.getDepartment() != null) {
            StudentStatus studentStatus = userProfile.getStudentStatus();
            String department = userProfile.getDepartment();
            int grade = userProfile.getGrade();
            int studentId = userProfile.getStudentId();
            userAdditionalProfile = new UserAdditionalProfile(studentStatus, department, grade, studentId);
        }
        return new UserProfileResponse(
                userProfile.getName(),
                userProfile.getNickname(),
                userProfile.getProfileImageUrl(),
                userAdditionalProfile,
                userProfile.getCreatedAt(),
                userProfile.getUpdatedAt()
        );
    }
}
