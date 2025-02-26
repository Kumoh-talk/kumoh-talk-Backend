package com.example.demo.domain.seminar_application.repository;

import com.example.demo.domain.seminar_application.domain.SeminarApplication;
import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.infra.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarApplicationRepository extends JpaRepository<SeminarApplication, Long> {
    Page<SeminarApplicationInfo> findByUser(User user, Pageable pageable);
}
