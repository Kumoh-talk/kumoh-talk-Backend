package com.example.demo.domain.newsletter.service;

import com.example.demo.domain.newsletter.domain.Newsletter;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterSubscribeRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateEmailRequest;
import com.example.demo.domain.newsletter.domain.dto.request.NewsletterUpdateNotifyRequest;
import com.example.demo.domain.newsletter.domain.dto.response.NewsletterInfo;
import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ServiceException;
import jakarta.validation.Valid;
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
    public void subscribe(Long userId, @Valid NewsletterSubscribeRequest request) {
        User user = userService.validateUser(userId);
        this.validateSubscribe(request.email());
        user.mapNewsletter(Newsletter.from(request));
    }
    
    public void validateSubscribe(String email) {
        if (newsletterRepository.existsByEmail(email)) {
            throw new ServiceException(SUBSCRIBE_EMAIL_CONFLICT);
        }
    }

    public NewsletterInfo getNewsletterInfo(Long userId) {
        User user = userService.validateUser(userId);
        if (user.getNewsletter() == null) {
            throw new ServiceException(SUBSCRIBE_NOT_FOUND);
        }
        return NewsletterInfo.from(user.getNewsletter());
    }

    @Transactional
    public void updateNewsletterEmail(Long userId, @Valid NewsletterUpdateEmailRequest request) {
        User user = userService.validateUser(userId);
        this.validateSubscribe(request.email());
        user.getNewsletter().updateNewsletterEmail(request);
    }

    @Transactional
    public void updateNewsletterNotify(Long userId, @Valid NewsletterUpdateNotifyRequest request) {
        User user = userService.validateUser(userId);
        user.getNewsletter().updateNewsletterNotify(request);
    }

    @Transactional
    public void deleteNewsletterInfo(Long userId) {
        User user = userService.validateUser(userId);
        Newsletter newsletter = user.getNewsletter();
        if (newsletter != null) {
            user.mapNewsletter(null);
        }
    }
}
