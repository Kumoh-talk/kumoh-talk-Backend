package com.example.demo.domain.newsletter.event;

import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.study_project_board.domain.dto.vo.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailNotificationEvent {
    private final BoardType boardType;
    private final EmailDeliveryStrategy emailStrategy;
}