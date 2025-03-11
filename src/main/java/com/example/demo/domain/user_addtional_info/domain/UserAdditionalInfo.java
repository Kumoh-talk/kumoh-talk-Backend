package com.example.demo.domain.user_addtional_info.domain;

import com.example.demo.domain.user_addtional_info.domain.dto.request.CreateUserAdditionalInfoRequest;
import com.example.demo.domain.user_addtional_info.domain.vo.StudentStatus;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_additional_infos")
@SQLDelete(sql = "UPDATE user_additional_infos SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class UserAdditionalInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String department; // 정해져있음.
    private int studentId;
    private int grade;
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;
    private String phoneNumber;
    private boolean isUpdated;

    public static UserAdditionalInfo from(CreateUserAdditionalInfoRequest request) {
        return UserAdditionalInfo.builder()
                .email(request.email())
                .department(request.department())
                .studentId(request.studentId())
                .grade(request.grade())
                .studentStatus(request.studentStatus())
                .phoneNumber(request.phoneNumber())
                .isUpdated(true)
                .build();
    }

    public void updateAcademicInfo(int grade, StudentStatus studentStatus) {
        this.grade = grade;
        this.studentStatus = studentStatus;
        this.isUpdated = true;
    }
}
