package com.example.demo.domain.seminar_application.service;

import com.example.demo.domain.seminar_application.domain.SeminarApplication;
import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationRequest;
import com.example.demo.domain.seminar_application.domain.dto.request.SeminarApplicationUpdateRequest;
import com.example.demo.domain.seminar_application.domain.dto.response.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import com.example.demo.domain.token.domain.dto.TokenResponse;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static com.example.demo.global.base.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeminarApplicationService {

    private final UserService userService;
    private final SeminarApplicationRepository seminarApplicationRepository;
    private final JwtHandler jwtHandler;

    @Transactional
    public Optional<TokenResponse> applyForSeminar(Long userId, @Valid SeminarApplicationRequest request) {
        User user = userService.validateUser(userId);
        boolean isFirstApplication = user.getSeminarApplications().isEmpty();

        if (isFirstApplication) { // 사용자 역할 업데이트 (첫 생성)
            user.updateUserRoleToSeminarWriter();
        }

        user.addSeminarApplications(SeminarApplication.from(request, user));

        return isFirstApplication
                ? Optional.of(jwtHandler.createTokens(JwtUserClaim.create(user))) // 첫 생성 시 토큰 반환
                : Optional.empty(); // 첫 생성이 아닐 경우 빈 Optional 반환
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
