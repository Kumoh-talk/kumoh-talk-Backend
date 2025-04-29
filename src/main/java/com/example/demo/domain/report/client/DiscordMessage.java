package com.example.demo.domain.report.client;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.newsletter.strategy.ByPassEmailDeliveryStrategy;
import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.user.entity.UserTarget;
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

    public static DiscordMessage createCommentReportMessage(UserTarget userTarget, CommentInfo commentInfo) {
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
                                                        + userTarget.getNickName()
                                                        + "\n"
                                                        + "### 📄 신고 댓글의 정보\n"
                                                        + "```json\n"
                                                        + "댓글 아이디 : " + commentInfo.getCommentId() + ",\n"
                                                        + "작성자 : " + commentInfo.getCommentUserInfo().getNickName() + ",\n"
                                                        + "댓글 정보 : " + commentInfo.getContent() + ",\n"
                                                        + "댓글 생성 시간 : " + commentInfo.getCreatedAt()
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
                                                        + (!(emailDeliveryStrategy instanceof ByPassEmailDeliveryStrategy)
                                                        ? "### 📄 알림 내용\n"
                                                        + "```json\n"
                                                        + "게시물 제목 : " + emailDeliveryStrategy.getVariables().get("title") + ",\n"
                                                        + "게시물 작성자 : " + emailDeliveryStrategy.getVariables().get("author") + ",\n"
                                                        + "게시물 URL : " + emailDeliveryStrategy.getVariables().get("postUrl") + ",\n"
                                                        + "\n```" : "\n"))
                                        .build()
                        )
                )
                .build();
    }
}
