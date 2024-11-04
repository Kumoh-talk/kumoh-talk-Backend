package com.example.demo.domain.newsletter.repository;

import com.example.demo.domain.newsletter.domain.Newsletter;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT n.email FROM Newsletter n WHERE " +
            "(:postType = 'SEMINAR_NOTICE' OR " +
            "(n.seminarContentNotice = true AND :postType = 'SEMINAR_SUMMARY') OR " +
            "(n.studyNotice = true AND :postType = 'STUDY') OR " +
            "(n.mentoringNotice = true AND :postType = 'MENTORING') OR " +
            "(n.projectNotice = true AND :postType = 'PROJECT'))")
    List<String> findSubscriberEmails(@Param("postType") String postType);
}
