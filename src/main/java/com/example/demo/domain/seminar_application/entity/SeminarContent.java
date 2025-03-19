package com.example.demo.domain.seminar_application.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SeminarContent {
    private LocalDate preferredDate;
    private String presentationTopic;
    private String seminarName;
    private String estimatedDuration;

    @Builder
    public SeminarContent(LocalDate preferredDate, String presentationTopic, String seminarName, String estimatedDuration) {
        this.preferredDate = preferredDate;
        this.presentationTopic = presentationTopic;
        this.seminarName = seminarName;
        this.estimatedDuration = estimatedDuration;
    }
}
