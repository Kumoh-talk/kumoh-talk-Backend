package com.example.demo.domain.newsletter.event;

import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.domain.newsletter.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsletterEventPublisher {
    private final NewsletterRepository newsletterRepository;
    private final EmailService emailService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EmailNotificationEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreatedEvent(EmailNotificationEvent event) {
        List<String> subscriberEmails = newsletterRepository.findSubscriberEmails(String.valueOf(event.getEntireBoardType()));
        emailService.sendEmailNotice(subscriberEmails, event.getEmailStrategy());
    }
}
