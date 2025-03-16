package com.example.demo.application.notification.controller;

import com.example.demo.application.notification.dto.response.NotificationNoOffsetResponse;
import com.example.demo.domain.notification.service.NotificationService;
import com.example.demo.global.aop.AssignUserId;
import com.example.demo.global.base.dto.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.global.base.dto.ResponseUtil.createSuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * [알림 페이지 조회]<br>
     * No-Offset으로 구현한 알림 페이지 조회 기능
     *
     * @param lastNotificationId 이전 페이지 마지막 알림물 Id(nullable)
     * @apiNote 1. 이전 페이지에서 출력한 가장 마지막 알림물의 Id를 lastNotificationId 실어 요청하면, 다음 알림부터 페이징 사이즈에 맞게 응답 <br>
     * 2. 가장 처음 요청을 위해 lastNotificationId는 nullable로 설정 <br>
     * -> lastNotificationId가 널이라면 서비스 로직에서 맨 처음 알림 Id를 조회하여 그 알림부터 페이징 사이즈에 맞게 응답 <br>
     * 3. 정렬 기준 : 생성 날짜 내림차순
     **/
    @AssignUserId
    @GetMapping("/my-notifications")
    public ResponseEntity<ResponseBody<NotificationNoOffsetResponse>> getUserNotification(
            Long userId,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Long lastNotificationId) {
        return ResponseEntity.ok(createSuccessResponse(
                NotificationNoOffsetResponse.from(notificationService.findNotificationListByNoOffset(userId, pageable, lastNotificationId))));
    }

    /**
     * [알림 읽음 처리]<br>
     * 알림 클릭 시 읽음 처리
     **/
    @AssignUserId
    @PostMapping("/my-notifications/{notificationId}")
    public ResponseEntity<ResponseBody<Void>> readUserNotification(
            Long userId,
            @PathVariable Long notificationId) {
        notificationService.readNotification(userId, notificationId);
        return ResponseEntity.ok(createSuccessResponse());
    }

    /**
     * [알림 삭제]<br>
     * 알림 hard delete
     **/
    @AssignUserId
    @DeleteMapping("/my-notifications/{notificationId}")
    public ResponseEntity<ResponseBody<Void>> deleteUserNotification(
            Long userId,
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(userId, notificationId);
        return ResponseEntity.ok(createSuccessResponse());
    }
}