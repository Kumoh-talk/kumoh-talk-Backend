package com.example.demo.application.newsletter.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public record EmailNoticeRequest(
        @NotBlank(message = "제목은 필수입니다.") String subject,
        @NotBlank(message = "HTML 내용은 필수입니다.") String htmlContent
) {
    public void validateHtmlContent() {
        try {
            // TODO. 정말 최소한의 검사. 좀 더 좋은 방법이 존재할까?
            Document document = Jsoup.parse(htmlContent, "", Parser.htmlParser());
            if (document.body().childrenSize() == 0) { // 본문이 존재하는지 체크
                throw new IllegalArgumentException("HTML 내용이 유효하지 않습니다. 본문이 없습니다.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("HTML 내용이 유효하지 않습니다: " + e.getMessage());
        }
    }
}
