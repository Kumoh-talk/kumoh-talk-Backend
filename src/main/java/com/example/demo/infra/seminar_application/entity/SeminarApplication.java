package com.example.demo.infra.seminar_application.entity;

import com.example.demo.domain.seminar_application.entity.SeminarApplicant;
import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.entity.SeminarContent;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "seminar_applications")
@SQLDelete(sql = "UPDATE seminar_applications SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class SeminarApplication extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String department;
    private int grade;
    private String studentId;
    private String phoneNumber;
    private LocalDate preferredDate;
    private String presentationTopic;
    private String seminarName;
    private String estimatedDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public SeminarApplication(String name, String department, int grade, String studentId, String phoneNumber, LocalDate preferredDate, String presentationTopic, String seminarName, String estimatedDuration, User user) {
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.preferredDate = preferredDate;
        this.presentationTopic = presentationTopic;
        this.seminarName = seminarName;
        this.estimatedDuration = estimatedDuration;
        this.user = user;
    }

    public static SeminarApplication from(SeminarApplicationInfo seminarApplicationInfo, User user) {
        return SeminarApplication.builder()
                .name(seminarApplicationInfo.getSeminarApplicant().getName())
                .department(seminarApplicationInfo.getSeminarApplicant().getDepartment())
                .grade(seminarApplicationInfo.getSeminarApplicant().getGrade())
                .studentId(seminarApplicationInfo.getSeminarApplicant().getStudentId())
                .phoneNumber(seminarApplicationInfo.getSeminarApplicant().getPhoneNumber())
                .preferredDate(seminarApplicationInfo.getSeminarContent().getPreferredDate())
                .presentationTopic(seminarApplicationInfo.getSeminarContent().getPresentationTopic())
                .seminarName(seminarApplicationInfo.getSeminarContent().getSeminarName())
                .estimatedDuration(seminarApplicationInfo.getSeminarContent().getEstimatedDuration())
                .user(user)
                .build();
    }

    public SeminarApplicationInfo toDomain() {
        return SeminarApplicationInfo.builder()
                .id(this.getId())
                .userTarget(UserTarget.builder()
                        .userId(user.getId())
                        .nickName(user.getNickname())
                        .userRole(user.getRole())
                        .build())
                .seminarApplicant(SeminarApplicant.builder()
                        .name(this.getName())
                        .department(this.getDepartment())
                        .grade(this.getGrade())
                        .studentId(this.getStudentId())
                        .phoneNumber(this.getPhoneNumber())
                        .build())
                .seminarContent(SeminarContent.builder()
                        .preferredDate(this.getPreferredDate())
                        .presentationTopic(this.getPresentationTopic())
                        .seminarName(this.getSeminarName())
                        .estimatedDuration(this.getEstimatedDuration())
                        .build())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

    public boolean canEditOrDelete() {
        LocalDate currentDate = LocalDate.now();
        LocalDate cutoffDate = preferredDate.minusDays(2); // 2일전까지는 편집/삭제 가능
        return currentDate.isBefore(cutoffDate) || currentDate.isEqual(cutoffDate);
    }

    public void updateSeminarApplicationInfo(SeminarApplicationInfo seminarApplicationInfo) {
        this.name = seminarApplicationInfo.getSeminarApplicant().getName();
        this.department = seminarApplicationInfo.getSeminarApplicant().getDepartment();
        this.grade = seminarApplicationInfo.getSeminarApplicant().getGrade();
        this.studentId = seminarApplicationInfo.getSeminarApplicant().getStudentId();
        this.phoneNumber = seminarApplicationInfo.getSeminarApplicant().getPhoneNumber();
        this.preferredDate = seminarApplicationInfo.getSeminarContent().getPreferredDate();
        this.presentationTopic = seminarApplicationInfo.getSeminarContent().getPresentationTopic();
        this.seminarName = seminarApplicationInfo.getSeminarContent().getSeminarName();
        this.estimatedDuration = seminarApplicationInfo.getSeminarContent().getEstimatedDuration();
    }
}
