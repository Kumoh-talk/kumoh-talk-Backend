package com.example.demo.domain.application.service;

import com.example.demo.domain.application.reposiotry.ApplicantRepository;
import com.example.demo.domain.application.reposiotry.ApplicationFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationFormRepository applicationFormRepository;
    private final ApplicantRepository applicantRepository;
}
