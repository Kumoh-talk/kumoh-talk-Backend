package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.client.DiscordNewsletterClient;
import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.report.client.DiscordMessage;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final DiscordNewsletterClient discordNewsletterClient;

    @Async
    public void sendEmailNotice(List<String> subscriberEmails, EmailDeliveryStrategy emailStrategy) {
        log.info("Trying to send Email to {}", subscriberEmails);
        // TODO. 성능에 이상이 있다면 bcc 방식으로 변경 예정
        for (String email : subscriberEmails) {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(new InternetAddress(email, "구독자"));
                mimeMessageHelper.setSubject(emailStrategy.getSubject());

                String htmlContent = generateHtmlContent(emailStrategy);
                mimeMessageHelper.setText(htmlContent, true);

                javaMailSender.send(mimeMessage);
                log.info("Succeeded to send Email to {}", subscriberEmails);
            } catch (Exception e) {
                log.info("Failed to send Email to {}, Error log: ", subscriberEmails, e);
                throw new RuntimeException(e);
            }
        }
        this.sendDiscordNotification(emailStrategy); // 모든 이메일 전송이 끝나면 디스코드로 알림
    }

    private String generateHtmlContent(EmailDeliveryStrategy emailStrategy) {
        Context context = new Context();
        context.setVariables(emailStrategy.getVariables());
        return templateEngine.process(emailStrategy.getTemplateName(), context);
    }

    private void sendDiscordNotification(EmailDeliveryStrategy emailStrategy) {
        discordNewsletterClient.sendNewsletter(DiscordMessage.createNewsletterMessage(emailStrategy));
    }
}
