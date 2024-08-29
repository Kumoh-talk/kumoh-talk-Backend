package com.example.demo.domain.seminar_application.service;

import com.example.demo.domain.seminar_application.domain.SeminarApplication;
import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationRequest;
import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationUpdateRequest;
import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.user_addtional_info.service.UserAdditionalInfoService;
import com.example.demo.global.base.exception.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.example.demo.global.base.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeminarApplicationService {

    private final UserService userService;
    private final UserAdditionalInfoService userAdditionalInfoService;
    private final SeminarApplicationRepository seminarApplicationRepository;

    @Transactional
    public void applyForSeminar(Long userId, @Valid SeminarApplicationRequest request) {
        User user = userService.validateUser(userId);
        userAdditionalInfoService.validateUserAdditionalInfo(user.getUserAdditionalInfo());
        user.addSeminarApplications(SeminarApplication.from(request, user));
    }

    public Page<SeminarApplicationInfo> getSeminarApplicationByUserId(Long userId, Pageable pageable) {
        User user = userService.validateUser(userId);
        return seminarApplicationRepository.findByUser(user, pageable);
    }

    @Transactional
    public void updateSeminarApplication(Long userId, Long seminarApplicationId, @Valid SeminarApplicationUpdateRequest request) {
        SeminarApplication savedApplication = seminarApplicationRepository.findById(seminarApplicationId)
                .orElseThrow(() -> new ServiceException(SEMINAR_APPLICATION_NOT_FOUND));
        this.validateCanEditOrDelete(savedApplication);
        this.validateIsApplicationOwner(userId, savedApplication.getUser().getId());
        savedApplication.updateSeminarApplicationInfo(request);
    }

    public void validateCanEditOrDelete(SeminarApplication seminarApplication) {
        if (!seminarApplication.canEditOrDelete()) {
            throw new ServiceException(SEMINAR_APPLICATION_CANNOT_EDIT_OR_DELETE);
        }
    }

    public void validateIsApplicationOwner(Long userId, Long applicationOwnerId) {
        if (!Objects.equals(userId, applicationOwnerId)) {
            throw new ServiceException(SEMINAR_APPLICATION_ACCESS_DENIED);
        }
    }

    @Transactional
    public void deleteSeminarApplication(Long userId, Long seminarApplicationId) {
        SeminarApplication savedApplication = seminarApplicationRepository.findById(seminarApplicationId)
                .orElseThrow(() -> new ServiceException(SEMINAR_APPLICATION_NOT_FOUND));
        this.validateCanEditOrDelete(savedApplication);
        this.validateIsApplicationOwner(userId, savedApplication.getUser().getId());
        seminarApplicationRepository.delete(savedApplication);
    }
}
