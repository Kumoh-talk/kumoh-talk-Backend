package com.example.demo.infra.newsletter.repository;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;
import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.infra.newsletter.entity.Newsletter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_EMAIL_CONFLICT;
import static com.example.demo.global.base.exception.ErrorCode.SUBSCRIBE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class NewsletterRepositoryImpl implements NewsletterRepository {
    private final NewsletterJpaRepository newsletterJpaRepository;

    @Override
    public void saveNewsletterSubscription(NewsletterSubscription newsletterSubscription) {
        newsletterJpaRepository.save(Newsletter.builder()
                .email(newsletterSubscription.getEmail())
                .seminarContentNotice(newsletterSubscription.getSeminarContentNotice())
                .studyNotice(newsletterSubscription.getStudyNotice())
                .projectNotice(newsletterSubscription.getProjectNotice())
                .mentoringNotice(newsletterSubscription.getMentoringNotice())
                .build());
    }

    @Override
    @Transactional
    public void updateNewsletterSubscription(NewsletterSubscription newsletterSubscription) {
        Newsletter newsletter = newsletterJpaRepository.findByEmail(newsletterSubscription.getEmail())
                .orElseThrow(() -> new ServiceException(SUBSCRIBE_NOT_FOUND));

        newsletter.updateNewsletter(newsletterSubscription.getEmail(),
                newsletterSubscription.getSeminarContentNotice(),
                newsletterSubscription.getStudyNotice(),
                newsletterSubscription.getProjectNotice(),
                newsletterSubscription.getMentoringNotice());
    }

    @Override
    @Transactional
    public void deleteNewsletterSubscription(String email) {
        Newsletter newsletter = newsletterJpaRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(SUBSCRIBE_NOT_FOUND));

        newsletterJpaRepository.delete(newsletter);
    }

    @Override
    public void validateNewsletterSubscription(String email) {
        if (newsletterJpaRepository.existsByEmail(email)) {
            throw new ServiceException(SUBSCRIBE_EMAIL_CONFLICT);
        }
    }

    @Override
    public List<String> findSubscriberEmails(String postType) {
        return newsletterJpaRepository.findSubscriberEmails(postType);
    }
}
