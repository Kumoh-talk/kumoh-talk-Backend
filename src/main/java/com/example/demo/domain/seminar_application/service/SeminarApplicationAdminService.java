package com.example.demo.domain.seminar_application.service;

import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
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

    public GlobalPageResponse<SeminarApplicationInfo> getAllSeminarApplications(Pageable pageable) {
        Page<SeminarApplicationInfo> pages = seminarApplicationRepository.findAll(pageable).map(SeminarApplicationInfo::from);
        return GlobalPageResponse.create(pages);
    }
}
