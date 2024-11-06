package com.example.demo.domain.newsletter.strategy;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudyNoticeEmailDeliveryStrategy implements EmailDeliveryStrategy {
    private String type;
    private String tag;
    private String title;
    private String author;
    private String link;

    @Override
    public String getTemplateName() {
        return "study_team_notice";
    }

    @Override
    public Map<String, Object> getVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("type", type);
        variables.put("tag", tag);
        variables.put("title", title);
        variables.put("author", author);
        variables.put("link", link);
        return variables;
    }

    @Override
    public String getSubject() {
        return "[야밤의금오톡] '스터디 팀원 공고' 새 글 알림";
    }

    public static StudyNoticeEmailDeliveryStrategy create(RecruitmentBoard recruitmentBoard) {
        if (!recruitmentBoard.getType().equals(RecruitmentBoardType.STUDY)) {
            throw new IllegalArgumentException("스터디에 대한 이메일 알림만 허용합니다.");
        }
        return new StudyNoticeEmailDeliveryStrategy(
                recruitmentBoard.getType().name(),
                recruitmentBoard.getTag().name(),
                recruitmentBoard.getTitle(),
                recruitmentBoard.getUser().getNickname(),
                "https://프론트도메인/~" // TODO. 수정 필요
        );
    }
}