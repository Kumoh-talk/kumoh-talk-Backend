package com.example.demo.domain.newsletter.event;

import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.domain.newsletter.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NewsletterEventListener {

    private final NewsletterRepository newsletterRepository;
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreatedEvent(EmailNotificationEvent event) {
        List<String> subscriberEmails = newsletterRepository.findSubscriberEmails(String.valueOf(event.getBoardType()));
        emailService.sendEmailNotice(subscriberEmails, event.getEmailStrategy());
    }
}
