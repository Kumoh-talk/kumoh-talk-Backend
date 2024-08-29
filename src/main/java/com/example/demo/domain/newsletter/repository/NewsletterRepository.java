package com.example.demo.domain.newsletter.repository;

import com.example.demo.domain.newsletter.domain.Newsletter;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {
    boolean existsByEmail(String email);
}
