package com.example.demo.domain.newsletter.domain.dto.response;

import com.example.demo.domain.newsletter.domain.Newsletter;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record NewsletterInfo(
        Long id,
        String email,
        Boolean seminarContentNotice,
        Boolean studyNotice,
        Boolean projectNotice,
        Boolean mentoringNotice,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt
) {
    public static NewsletterInfo from(Newsletter newsletter) {
        return new NewsletterInfo(
                newsletter.getId(),
                newsletter.getEmail(),
                newsletter.getSeminarContentNotice(),
                newsletter.getStudyNotice(),
                newsletter.getProjectNotice(),
                newsletter.getMentoringNotice(),
                newsletter.getCreatedAt(),
                newsletter.getUpdatedAt()
        );
    }
}
