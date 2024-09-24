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
            "(n.isSeminarContentUpdated = true AND :postType = 'SEMINAR_SUMMARY') OR " +
            "(n.isStudyUpdated = true AND :postType = 'STUDY') OR " +
            "(n.isProjectUpdated = true AND :postType = 'PROJECT'))")
    List<String> findSubscriberEmails(@Param("postType") String postType);
}
