package com.example.demo.application.notification.dto.response;

import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.vo.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "NotificationResponse", description = "알림 세부 정보 응답")
public class NotificationResponse {
    @Schema(description = "알림 id 정보", example = "1")
    private Long id;
    @Schema(description = "모집 게시물 타입 정보[BOARD_LIKE, BOARD_COMMENT, RECRUITMENT_BOARD_COMMENT]", example = "LIKE")
    private NotificationType invokerType;
    @Schema(description = "알림을 발생시킨 콘텐츠 아이디(좋아요, 댓글 등)", example = "1")
    private Long invokerId;
    @Schema(description = "알림을 누르면 이동할 게시물 id", example = "3")
    private Long boardId;
    @Schema(description = "알림을 보낸 유저 닉네임 정보", example = "kumoh")
    private String senderNickname;
    @Schema(description = "알림 조회 정보", example = "true")
    private boolean isRead;

    public static NotificationResponse from(NotificationInfo notificationInfo) {
        return NotificationResponse.builder()
                .id(notificationInfo.getNotificationId())
                .invokerType(notificationInfo.getInvokerType())
                .invokerId(notificationInfo.getInvokerId())
                .boardId(notificationInfo.getBoardId())
                .senderNickname(notificationInfo.getSenderNickname())
                .isRead(notificationInfo.isRead())
                .build();
    }
}
