package com.example.demo.domain.newsletter.event;

import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.domain.vo.BoardType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailNotificationEvent {
    private final BoardType boardType;
    private final EmailDeliveryStrategy emailStrategy;

    public static EmailNotificationEvent create(BoardType boardType, EmailDeliveryStrategy emailStrategy) {
        return new EmailNotificationEvent(boardType, emailStrategy);
    }
}