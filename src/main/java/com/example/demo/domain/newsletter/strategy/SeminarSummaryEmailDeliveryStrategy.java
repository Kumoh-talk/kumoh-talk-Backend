package com.example.demo.domain.newsletter.strategy;

import com.example.demo.application.board.dto.vo.BoardType;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.infra.board.entity.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SeminarSummaryEmailDeliveryStrategy extends BaseEmailDeliveryStrategy {
    private String title;
    private String author;
    private String postUrl;

    @Override
    public String getTemplateName() {
        return "seminar_summary";
    }

    @Override
    public Map<String, Object> getVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", title);
        variables.put("author", author);
        variables.put("postUrl", postUrl);
        variables.put("youtubeUrl", youtubeUrl);
        variables.put("changeSubscribeUrl", changeSubscribeUrl);
        variables.put("cancelSubscribeUrl", cancelSubscribeUrl);
        return variables;
    }

    @Override
    public String getSubject() {
        return "[야밤의금오톡] '세미나 발표 정리' 새 글 알림";
    }

    public static SeminarSummaryEmailDeliveryStrategy create(BoardInfo boardInfo) {
        if (!boardInfo.getBoardContent().getBoardType().equals(BoardType.SEMINAR)) {
            throw new IllegalArgumentException("세미나 내용 정리에 대한 이메일 알림만 허용합니다.");
        }
        return new SeminarSummaryEmailDeliveryStrategy(// TODO. 프론트 배포 후 수정 필요
            boardInfo.getBoardContent().getTitle(),
                boardInfo.getUserTarget().getNickName(),
                "https://프론트도메인/~"
        );
    }
}