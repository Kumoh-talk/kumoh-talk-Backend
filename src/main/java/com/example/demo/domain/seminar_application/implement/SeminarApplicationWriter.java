package com.example.demo.domain.seminar_application.implement;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeminarApplicationWriter {
    private final SeminarApplicationRepository seminarApplicationRepository;

    public void addSeminarApplication(Long userId, SeminarApplicationInfo request) {
        seminarApplicationRepository.addSeminarApplication(userId, request);
    }

    public void updateSeminarApplication(Long id, SeminarApplicationInfo request) {
        seminarApplicationRepository.updateSeminarApplication(id, request);
    }

    public void deleteSeminarApplication(Long id) {
        seminarApplicationRepository.deleteById(id);
    }

}
