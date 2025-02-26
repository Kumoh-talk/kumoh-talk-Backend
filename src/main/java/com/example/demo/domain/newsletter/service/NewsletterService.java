package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;
import com.example.demo.domain.newsletter.implement.NewsletterHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsletterService {
    private final NewsletterHandler newsletterHandler;

    @Transactional
    public void subscribe(NewsletterSubscription newsletterSubscription) {
        newsletterHandler.validateNewsletterSubscription(newsletterSubscription.getEmail());
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
        newsletterHandler.updateNewsletterSubscription(newsletterSubscription);
    }

    public void deleteNewsletterInfo(String email) {
        newsletterHandler.deleteNewsletterSubscription(email);
    }
}
