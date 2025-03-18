package com.example.demo.application.notification.dto.response;

import com.example.demo.domain.notification.entity.NotificationInfo;
import com.example.demo.domain.notification.entity.NotificationSliceInfo;
import com.example.demo.global.base.dto.page.GlobalNoOffsetPageResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
@Schema(name = "NotificationNoOffsetResponse", description = "no-offset 방식 알림 페이지 응답")
public class NotificationNoOffsetResponse extends GlobalNoOffsetPageResponse<NotificationResponse> {
    @Schema(description = "전체 페이지에서 읽지 않은 알림 갯수", example = "3")
    private Long unreadNotificationCount;

    public NotificationNoOffsetResponse(Long unreadNotificationCount, Slice<NotificationInfo> pageContents) {
        super(pageContents.hasNext(), pageContents.getSize(), pageContents.getContent().stream().map(NotificationResponse::from).toList());
        this.unreadNotificationCount = unreadNotificationCount;
    }

    public static NotificationNoOffsetResponse from(NotificationSliceInfo notificationSliceInfo) {
        return new NotificationNoOffsetResponse(
                notificationSliceInfo.getUnreadNotificationCount(), notificationSliceInfo.getNotificationSlice());
    }
}
