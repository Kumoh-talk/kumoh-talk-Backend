package com.example.demo.domain.user_addtional_info.entity;

import com.example.demo.application.user_additional_info.dto.vo.StudentStatus;
import com.example.demo.infra.user_additional_info.entity.UserAdditionalInfo;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class UserAdditionalInfoData {
    private Long id;
    private String email;
    private String department; // 정해져있음.
    private int studentId;
    private int grade;
    private StudentStatus studentStatus;
    private String phoneNumber;
    private boolean isUpdated;

    public static UserAdditionalInfoData from(UserAdditionalInfo userAdditionalInfo) {
        return UserAdditionalInfoData.builder()
                .email(userAdditionalInfo.getEmail())
                .department(userAdditionalInfo.getDepartment())
                .studentId(userAdditionalInfo.getStudentId())
                .grade(userAdditionalInfo.getGrade())
                .studentStatus(userAdditionalInfo.getStudentStatus())
                .phoneNumber(userAdditionalInfo.getPhoneNumber())
                .isUpdated(true)
                .build();
    }
}
