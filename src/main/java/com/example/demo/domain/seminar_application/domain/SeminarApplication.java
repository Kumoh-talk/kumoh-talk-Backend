package com.example.demo.domain.seminar_application.domain;

import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
    private String preferredDate;
    private String presentationTopic;
    private String seminarName;
    private String topicDescription;
    private String estimatedDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public SeminarApplication(String name, String department, int grade, String studentId, String phoneNumber, String preferredDate, String presentationTopic, String seminarName, String topicDescription, String estimatedDuration) {
        this.name = name;
        this.department = department;
        this.grade = grade;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.preferredDate = preferredDate;
        this.presentationTopic = presentationTopic;
        this.seminarName = seminarName;
        this.topicDescription = topicDescription;
        this.estimatedDuration = estimatedDuration;
    }
}
