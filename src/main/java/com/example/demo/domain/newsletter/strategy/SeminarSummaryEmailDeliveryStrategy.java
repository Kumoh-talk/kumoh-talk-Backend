package com.example.demo.domain.newsletter.strategy;

import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.domain.board.domain.entity.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SeminarSummaryEmailDeliveryStrategy implements EmailDeliveryStrategy {
    private String title;
    private String author;
    private String link;

    @Override
    public String getTemplateName() {
        return "seminar_notice";
    }

    @Override
    public Map<String, Object> getVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", title);
        variables.put("author", author);
        variables.put("link", link);
        return variables;
    }

    @Override
    public String getSubject() {
        return "[야밤의금오톡] '세미나 발표 정리' 새 글 알림";
    }

    public static SeminarSummaryEmailDeliveryStrategy create(Board board) {
        if (!board.getBoardType().equals(BoardType.SEMINAR)) {
            throw new IllegalArgumentException("세미나 내용 정리에 대한 이메일 알림만 허용합니다.");
        }
        return new SeminarSummaryEmailDeliveryStrategy(
                board.getTitle(),
                board.getUser().getNickname(),
                "https://프론트도메인/~" // TODO. 수정 필요
        );
    }
}