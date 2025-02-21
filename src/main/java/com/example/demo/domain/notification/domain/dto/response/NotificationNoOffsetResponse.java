package com.example.demo.domain.notification.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@Schema(name = "NotificationNoOffsetResponse", description = "no-offset 방식 알림 페이지 응답")
public class NotificationNoOffsetResponse {
    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean nextPage;
    @Schema(description = "한 페이지에 게시물 갯수", example = "10")
    private int pageSize;
    @Schema(description = "전체 페이지에서 읽지 않은 알림 갯수", example = "3")
    private Long unreadNotificationCount;
    @Schema(description = "알림 세부 정보 리스트")
    private List<NotificationResponse> notificationList;

    public static NotificationNoOffsetResponse from(boolean nextPage, int pageSize, Long count, List<NotificationResponse> notificationResponseList) {
        return NotificationNoOffsetResponse.builder()
                .nextPage(nextPage)
                .pageSize(pageSize)
                .unreadNotificationCount(count)
                .notificationList(notificationResponseList)
                .build();
    }
}
