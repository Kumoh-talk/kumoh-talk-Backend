package com.example.demo.domain.seminar_application.repository;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SeminarApplicationRepository {
    Optional<SeminarApplicationInfo> findById(Long id);

    boolean isFirstApplication(Long userId);

    void addSeminarApplication(Long userId, SeminarApplicationInfo seminarApplicationInfo);

    void updateSeminarApplication(Long id, SeminarApplicationInfo seminarApplicationInfo);

    boolean canEditOrDelete(Long id);

    void deleteById(Long id);

    Page<SeminarApplicationInfo> findByUserId(Long userId, Pageable pageable);

    Page<SeminarApplicationInfo> findAll(Pageable pageable);
}
