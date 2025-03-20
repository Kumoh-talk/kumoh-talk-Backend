package com.example.demo.domain.seminar_application.service;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeminarApplicationAdminService {

    private final SeminarApplicationRepository seminarApplicationRepository;

    public Page<SeminarApplicationInfo> getAllSeminarApplications(Pageable pageable) {
        return seminarApplicationRepository.findAll(pageable);
    }
}
