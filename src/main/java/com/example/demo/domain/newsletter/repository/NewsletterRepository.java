package com.example.demo.domain.newsletter.repository;

import com.example.demo.domain.newsletter.entity.NewsletterSubscription;

import java.util.List;

public interface NewsletterRepository {
    void saveNewsletterSubscription(NewsletterSubscription newsletterSubscription);

    void updateNewsletterSubscription(NewsletterSubscription newsletterSubscription);

    void deleteNewsletterSubscription(String email);

    boolean existsNewsletterSubscription(String email);

    List<String> findSubscriberEmails(String postType);
}
