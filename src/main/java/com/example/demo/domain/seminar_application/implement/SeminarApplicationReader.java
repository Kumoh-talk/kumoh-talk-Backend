package com.example.demo.domain.seminar_application.implement;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeminarApplicationReader {
    private final SeminarApplicationRepository seminarApplicationRepository;

    public Optional<SeminarApplicationInfo> findById(Long id) {
        return seminarApplicationRepository.findById(id);
    }

    public boolean isFirstApplication(Long userId) {
        return seminarApplicationRepository.isFirstApplication(userId);
    }

    public boolean canEditOrDelete(Long id) {
        return seminarApplicationRepository.canEditOrDelete(id);
    }

    public Page<SeminarApplicationInfo> getSeminarApplicationByUserId(Long userId, Pageable pageable) {
        return seminarApplicationRepository.findByUserId(userId, pageable);
    }
}
