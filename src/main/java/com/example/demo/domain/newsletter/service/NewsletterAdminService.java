package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.entity.SeminarNoticeContent;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.newsletter.event.NewsletterEventPublisher;
import com.example.demo.domain.newsletter.strategy.ByPassEmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewsletterAdminService {
    private final NewsletterEventPublisher newsletterEventPublisher;
    private final TemplateEngine templateEngine;

    public void sendNoticeByEmail(String subject, String htmlContent) {
        EmailNotificationEvent emailNotificationEvent = EmailNotificationEvent.create(
                EntireBoardType.SEMINAR_NOTICE,
                ByPassEmailDeliveryStrategy.create(subject, htmlContent));

        newsletterEventPublisher.publish(emailNotificationEvent);
    }

    public SeminarNoticeContent getSeminarNoticeBasicForm() {
        ByPassEmailDeliveryStrategy byPassEmailDeliveryStrategy = new ByPassEmailDeliveryStrategy();
        Context context = new Context();
        context.setVariables(byPassEmailDeliveryStrategy.getVariables());
        String html = templateEngine.process(byPassEmailDeliveryStrategy.getTemplateName(), context);
        return new SeminarNoticeContent(html);
    }
}
