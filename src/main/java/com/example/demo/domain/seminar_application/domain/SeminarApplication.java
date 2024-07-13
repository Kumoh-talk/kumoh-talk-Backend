package com.example.demo.domain.seminar_application.domain;

import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SeminarApplication extends BaseEntity { // TODO. 수정,삭제가 필요할 시 soft delete 적용

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
    private boolean isYoutubeUploadConsented;
    private boolean isBlogPostWritten;

    @Builder
    public SeminarApplication(String name, String department, int grade, String studentId, String phoneNumber, String preferredDate, String presentationTopic, String seminarName, String topicDescription, String estimatedDuration, boolean isYoutubeUploadConsented, boolean isBlogPostWritten) {
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
        this.isYoutubeUploadConsented = isYoutubeUploadConsented;
        this.isBlogPostWritten = isBlogPostWritten;
    }
}
