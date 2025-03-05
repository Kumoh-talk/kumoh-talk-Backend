package com.example.demo.domain.newsletter.event;

import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.entity.vo.EntireBoardType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailNotificationEvent {
    private final EntireBoardType entireBoardType;
    private final EmailDeliveryStrategy emailStrategy;

    public static EmailNotificationEvent create(EntireBoardType entireBoardType, EmailDeliveryStrategy emailStrategy) {
        return new EmailNotificationEvent(entireBoardType, emailStrategy);
    }
}