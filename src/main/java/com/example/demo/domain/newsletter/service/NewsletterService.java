package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;
import com.example.demo.domain.newsletter.implement.NewsletterHandler;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_EMAIL_CONFLICT;
import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsletterService {
    private final NewsletterHandler newsletterHandler;

    @Transactional
    public void subscribe(NewsletterSubscription newsletterSubscription) {
        validateNewsletterSubscriptionNotExists(newsletterSubscription.getEmail());
        newsletterHandler.saveNewsletterSubscription(newsletterSubscription);
        log.info("뉴스레터 구독이 완료되었습니다. 구독자 이메일: {}", newsletterSubscription.getEmail());
    }

    @Transactional
    public void updateNewsletterNotify(NewsletterSubscription newsletterSubscription) {
        validateNewsletterSubscriptionExists(newsletterSubscription.getEmail());
        newsletterHandler.updateNewsletterSubscription(newsletterSubscription);
        log.info("뉴스레터 구독 정보가 갱신되었습니다. 구독자 이메일: {}", newsletterSubscription.getEmail());
    }

    @Transactional
    public void deleteNewsletterInfo(String email) {
        validateNewsletterSubscriptionExists(email);
        newsletterHandler.deleteNewsletterSubscription(email);
        log.info("뉴스레터 구독이 취소되었습니다. 구독자 이메일: {}", email);
    }

    private void validateNewsletterSubscriptionNotExists(String email) {
        if (newsletterHandler.existsNewsletterSubscription(email)) {
            throw new ServiceException(SUBSCRIBE_EMAIL_CONFLICT);
        }
    }

    private void validateNewsletterSubscriptionExists(String email) {
        if (!newsletterHandler.existsNewsletterSubscription(email)) {
            throw new ServiceException(SUBSCRIBE_NOT_FOUND);
        }
    }
}
