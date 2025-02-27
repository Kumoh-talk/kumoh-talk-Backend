package com.example.demo.infra.newsletter.repository;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;
import com.example.demo.domain.newsletter.repository.NewsletterRepository;
import com.example.demo.infra.newsletter.entity.Newsletter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        newsletterJpaRepository.findByEmail(newsletterSubscription.getEmail())
                .ifPresent(newsletter -> {
                    newsletter.updateNewsletter(newsletterSubscription.getEmail(),
                            newsletterSubscription.getSeminarContentNotice(),
                            newsletterSubscription.getStudyNotice(),
                            newsletterSubscription.getProjectNotice(),
                            newsletterSubscription.getMentoringNotice());
                });
    }

    @Override
    @Transactional
    public void deleteNewsletterSubscription(String email) {
        newsletterJpaRepository.deleteByEmail(email);
    }

    @Override
    public boolean existsNewsletterSubscription(String email) {
        return newsletterJpaRepository.existsByEmail(email);
    }

    @Override
    public List<String> findSubscriberEmails(String postType) {
        return newsletterJpaRepository.findSubscriberEmails(postType);
    }
}
