package com.example.demo.domain.report.client;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DiscordMessage {

    private String content;
    private List<Embed> embeds;

    @Builder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class Embed {
        private String title;
        private String description;
    }

    public static DiscordMessage createCommentReportMessage(User user, Comment comment) {
        return DiscordMessage.builder()
                .content("# 🚨 댓글 신고가 접수되었습니다!!!")
                .embeds(
                        List.of(
                                Embed.builder()
                                        .title("ℹ️ 신고 댓글 정보")
                                        .description(
                                                "### 🕖 발생 시간\n"
                                                        + LocalDateTime.now()
                                                        + "\n"
                                                        + "### 🔗 신고한 사람\n"
                                                        + user.getNickname()
                                                        + "\n"
                                                        + "### 📄 신고 댓글의 정보\n"
                                                        + "```json\n"
                                                        + "댓글 아이디 : " + comment.getId() + ",\n"
                                                        + "작성자 : " + comment.getUser().getNickname() + ",\n"
                                                        + "댓글 정보 : " + comment.getContent() + ",\n"
                                                        + "댓글 생성 시간 : " + comment.getCreatedAt()
                                                        + "\n```")
                                        .build()
                        )
                )
                .build();
    }

    public static DiscordMessage createNewsletterMessage(EmailDeliveryStrategy emailDeliveryStrategy) {
        return DiscordMessage.builder()
                .content("# 📬 이메일 알림이 전송되었습니다!!!")
                .embeds(
                        List.of(
                                Embed.builder()
                                        .title("ℹ️ 알림 정보")
                                        .description(
                                                "### 🕖 발생 시간\n"
                                                        + LocalDateTime.now()
                                                        + "\n"
                                                        + "### 🎈 알림 주제\n"
                                                        + emailDeliveryStrategy.getSubject()
                                                        + "\n")
                                        .build()
                        )
                )
                .build();
    }
}