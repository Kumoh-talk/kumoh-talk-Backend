package com.example.demo.domain.newsletter.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsletterEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EmailNotificationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
