package com.example.demo.domain.seminar_application.domain;

import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationRequest;
import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationUpdateRequest;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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

    public static SeminarApplication from(SeminarApplicationRequest request, User user) {
        return SeminarApplication.builder()
                .name(request.name())
                .department(request.department())
                .grade(request.grade())
                .studentId(request.studentId())
                .phoneNumber(request.phoneNumber())
                .preferredDate(request.preferredDate())
                .presentationTopic(request.presentationTopic())
                .seminarName(request.seminarName())
                .estimatedDuration(request.estimatedDuration())
                .user(user)
                .build();
    }

    public boolean canEditOrDelete() {
        LocalDate currentDate = LocalDate.now();
        LocalDate cutoffDate = preferredDate.minusDays(2); // 2일전까지는 편집/삭제 가능
        return currentDate.isBefore(cutoffDate) || currentDate.isEqual(cutoffDate);
    }

    public void updateSeminarApplicationInfo(@Valid SeminarApplicationUpdateRequest request) {
        this.name = request.name();
        this.department = request.department();
        this.grade = request.grade();
        this.studentId = request.studentId();
        this.phoneNumber = request.phoneNumber();
        this.preferredDate = request.preferredDate();
        this.presentationTopic = request.presentationTopic();
        this.seminarName = request.seminarName();
        this.estimatedDuration = request.estimatedDuration();
    }
}
