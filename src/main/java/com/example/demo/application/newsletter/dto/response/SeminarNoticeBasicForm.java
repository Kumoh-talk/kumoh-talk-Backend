package com.example.demo.application.newsletter.dto.response;

import com.example.demo.domain.newsletter.entity.SeminarNoticeContent;

public record SeminarNoticeBasicForm(
        String seminarNoticeBasicFrom
) {
    public static SeminarNoticeBasicForm of(SeminarNoticeContent seminarNoticeContent) {
        return new SeminarNoticeBasicForm(seminarNoticeContent.getHtmlContent());
    }
}
