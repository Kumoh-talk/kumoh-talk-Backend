package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.domain.dto.response.SeminarNoticeBasicForm;
import com.example.demo.domain.newsletter.event.EmailNotificationEvent;
import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.domain.newsletter.strategy.ByPassEmailDeliveryStrategy;
import com.example.demo.domain.recruitment_board.domain.dto.vo.BoardType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NewsletterAdminService {

    private final NewsletterRepository newsletterRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TemplateEngine templateEngine;

    public void sendNoticeByEmail(String subject, String htmlContent) {
        eventPublisher.publishEvent(
                EmailNotificationEvent.create(
                        BoardType.SEMINAR_NOTICE,
                        ByPassEmailDeliveryStrategy.create(subject, htmlContent)));
    }

    public SeminarNoticeBasicForm getSeminarNoticeBasicForm() {
        ByPassEmailDeliveryStrategy byPassEmailDeliveryStrategy = new ByPassEmailDeliveryStrategy();
        Context context = new Context();
        context.setVariables(byPassEmailDeliveryStrategy.getVariables());
        String html = templateEngine.process(byPassEmailDeliveryStrategy.getTemplateName(), context);
        return new SeminarNoticeBasicForm(html);
    }
}
