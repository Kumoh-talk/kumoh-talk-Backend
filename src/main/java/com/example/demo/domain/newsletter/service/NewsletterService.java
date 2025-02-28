package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;
import com.example.demo.domain.newsletter.implement.NewsletterHandler;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_EMAIL_CONFLICT;
import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsletterService {
    private final NewsletterHandler newsletterHandler;

    @Transactional
    public void subscribe(NewsletterSubscription newsletterSubscription) {
        validateNewsletterSubscriptionNotExists(newsletterSubscription.getEmail());
        newsletterHandler.saveNewsletterSubscription(newsletterSubscription);
    }

//    public NewsletterInfo getNewsletterInfo(Long userId) {
//        User user = userService.validateUser(userId);
//        if (!user.hasNewsletter()) {
//            throw new ServiceException(SUBSCRIBE_NOT_FOUND);
//        }
//        return NewsletterInfo.from(user.getNewsletter());
//    }

//    @Transactional
//    public void updateNewsletterEmail(NewsletterUpdateEmailRequest request) {
//        this.validateSubscribe(request.email());
//        Newsletter newsletter = newsletterRepository.findByEmail(request.email())
//                .orElseThrow(() -> new ServiceException(SUBSCRIBE_NOT_FOUND));
//        newsletter.updateNewsletter(request);
//    }

    public void updateNewsletterNotify(NewsletterSubscription newsletterSubscription) {
        validateNewsletterSubscriptionExists(newsletterSubscription.getEmail());
        newsletterHandler.updateNewsletterSubscription(newsletterSubscription);
    }

    public void deleteNewsletterInfo(String email) {
        validateNewsletterSubscriptionExists(email);
        newsletterHandler.deleteNewsletterSubscription(email);
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
