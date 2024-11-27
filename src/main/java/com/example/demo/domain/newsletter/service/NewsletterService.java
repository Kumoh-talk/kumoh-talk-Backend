package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.domain.Newsletter;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterSubscribeRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateNotifyRequest;
import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ServiceException;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_EMAIL_CONFLICT;
import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NewsletterService {

    private final UserService userService;
    private final NewsletterRepository newsletterRepository;

    @Transactional
    public void subscribe(NewsletterSubscribeRequest request) {
        this.validateSubscribe(request.email());
        newsletterRepository.save(Newsletter.from(request));
    }
    
    public void validateSubscribe(String email) {
        if (newsletterRepository.existsByEmail(email)) {
            throw new ServiceException(SUBSCRIBE_EMAIL_CONFLICT);
        }
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

    @Transactional
    public void updateNewsletterNotify(String email, NewsletterUpdateNotifyRequest request) {
        Newsletter newsletter = newsletterRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(SUBSCRIBE_NOT_FOUND));
        this.validateSubscribe(request.email());
        newsletter.updateNewsletter(request);
    }

    @Transactional
    public void deleteNewsletterInfo(String email) {
        Newsletter newsletter = newsletterRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(SUBSCRIBE_NOT_FOUND));
        newsletterRepository.delete(newsletter);
    }
}
