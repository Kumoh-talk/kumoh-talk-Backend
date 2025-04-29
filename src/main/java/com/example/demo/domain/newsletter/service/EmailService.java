package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.client.DiscordNewsletterClient;
import com.example.demo.domain.newsletter.strategy.ByPassEmailDeliveryStrategy;
import com.example.demo.domain.newsletter.strategy.EmailDeliveryStrategy;
import com.example.demo.domain.report.client.DiscordMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
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

import java.io.UnsupportedEncodingException;
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
        log.info("이메일 전송을 시작합니다. 이메일 제목: {}, 구독자 이메일 목록({}명): {}", emailStrategy.getSubject(), subscriberEmails.size(), subscriberEmails);

        int failed = 0;
        for (String email : subscriberEmails) {
            try {
                MimeMessage mimeMessage = createMimeMessage(email, emailStrategy);
                javaMailSender.send(mimeMessage);
                log.debug("이메일 전송에 성공했습니다. 이메일: {}, 이메일 제목: {}", email, emailStrategy.getSubject());
            } catch (AddressException e) {
                log.warn("유효하지 않은 이메일 주소입니다. 이메일: {}, 오류 메시지: {}, 이메일 제목: {}", email, e.getMessage(), emailStrategy.getSubject());
                ++failed;
            } catch (MessagingException e) {
                log.warn("이메일 전송 중 오류가 발생했습니다. 이메일: {}, 오류 메시지: {}, 이메일 제목: {}", email, e.getMessage(), emailStrategy.getSubject());
                ++failed;
            } catch (Exception e) {
                log.error("이메일 전송 중 예상치 못한 오류가 발생했습니다. 이메일: {}, 오류 메시지: {}, 이메일 제목: {}", email, e.getMessage(), emailStrategy.getSubject());
                ++failed;
            } // 오류가 나는 이메일에 대해 로그만 찍고, 다른 모든 메일에 대해 전송 시도
        }

        log.info("이메일 전송을 완료했습니다. 이메일 제목: {}, 총 {}명 중 {}명 성공, {}명 실패", emailStrategy.getSubject(), subscriberEmails.size(), subscriberEmails.size() - failed, failed);
        sendDiscordNotification(emailStrategy); // 모든 이메일 전송이 끝나면 디스코드로 알림
    }

    private MimeMessage createMimeMessage(String email, EmailDeliveryStrategy emailStrategy) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(new InternetAddress(email, "구독자", "UTF-8"));
        mimeMessageHelper.setSubject(emailStrategy.getSubject());

        String htmlContent = getHtmlContent(emailStrategy);
        mimeMessageHelper.setText(htmlContent, true);

        return mimeMessage;
    }

    private String getHtmlContent(EmailDeliveryStrategy emailStrategy) {
        if (emailStrategy instanceof ByPassEmailDeliveryStrategy) {
            return ((ByPassEmailDeliveryStrategy) emailStrategy).getHtmlContent();
        } else {
            return generateHtmlContent(emailStrategy);
        }
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
