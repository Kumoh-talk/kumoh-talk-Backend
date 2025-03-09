package com.example.demo.domain.recruitment_application.implement;

import com.example.demo.domain.recruitment_application.entity.RecruitmentApplicationInfo;
import com.example.demo.domain.recruitment_application.repository.RecruitmentApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecruitmentApplicationWriter {
    private final RecruitmentApplicationRepository recruitmentApplicationRepository;

    public RecruitmentApplicationInfo post(RecruitmentApplicationInfo recruitmentApplicationInfo, Long recruitmentBoardId) {
        return recruitmentApplicationRepository.post(recruitmentApplicationInfo, recruitmentBoardId);
    }

    public RecruitmentApplicationInfo patch(
            RecruitmentApplicationInfo originApplicationInfo, RecruitmentApplicationInfo newApplicationInfo) {
        return recruitmentApplicationRepository.patch(originApplicationInfo, newApplicationInfo);
    }

    public void delete(RecruitmentApplicationInfo recruitmentApplicationInfo) {
        recruitmentApplicationRepository.delete(recruitmentApplicationInfo);
    }
}
