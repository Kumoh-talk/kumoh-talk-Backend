package com.example.demo.domain.seminar_application.service;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.implement.SeminarApplicationReader;
import com.example.demo.domain.seminar_application.implement.SeminarApplicationWriter;
import com.example.demo.domain.token.entity.Token;
import com.example.demo.domain.user.entity.UpdateUserInfo;
import com.example.demo.domain.user.entity.UserInfo;
import com.example.demo.domain.user.entity.UserTarget;
import com.example.demo.domain.user.implement.UserReader;
import com.example.demo.domain.user.implement.UserWriter;
import com.example.demo.domain.user.vo.Role;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import com.example.demo.global.jwt.JwtHandler;
import com.example.demo.global.jwt.JwtUserClaim;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SeminarApplicationService {

    private final SeminarApplicationReader seminarApplicationReader;
    private final SeminarApplicationWriter seminarApplicationWriter;

    private final UserReader userReader;
    private final UserWriter userWriter;
    private final JwtHandler jwtHandler;

    @Transactional
    public Optional<Token> applyForSeminar(Long userId, SeminarApplicationInfo request) {
        userReader.validateUser(userId);
        UserInfo userInfo = userReader.getUserInfo(userId);
        Role userRole = userInfo.getRole();

        // 세미나 처음 지원할 때 세미나 게시물 작성 권한 부여
        boolean isFirstApplication = seminarApplicationReader.isFirstApplication(userId);
        if (isFirstApplication && userInfo.getRole() != Role.ROLE_ADMIN) { // 관리자면 권한 갱신을 하지 않음
            userWriter.updateUserInfo(userId,
                    UpdateUserInfo.builder()
                            .nickname(userInfo.getNickname())
                            .name(userInfo.getName())
                            .profileImageUrl(userInfo.getProfileImageUrl())
                            .role(Role.ROLE_SEMINAR_WRITER)
                            .build());
            userRole = Role.ROLE_SEMINAR_WRITER;
        }

        seminarApplicationWriter.addSeminarApplication(userId, request);

        return isFirstApplication
                ? Optional.of(jwtHandler.createTokens(JwtUserClaim.create(userId, userRole))) // 첫 생성 시 토큰 반환
                : Optional.empty(); // 첫 생성이 아닐 경우 빈 Optional 반환
    }

    public Page<SeminarApplicationInfo> getSeminarApplicationByUserId(Long userId, Pageable pageable) {
        UserTarget userTarget = userReader.findUserTarget(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        return seminarApplicationReader.getSeminarApplicationByUserId(userTarget.getUserId(), pageable);
    }

    @Transactional
    public void updateSeminarApplication(Long userId, Long seminarApplicationId, SeminarApplicationInfo request) {
        SeminarApplicationInfo savedApplication = seminarApplicationReader.findById(seminarApplicationId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SEMINAR_APPLICATION_NOT_FOUND));

        this.validateCanEditOrDelete(savedApplication);
        this.validateIsApplicationOwner(userId, savedApplication.getUserTarget().getUserId());

        seminarApplicationWriter.updateSeminarApplication(seminarApplicationId, request);
    }

    public void validateCanEditOrDelete(SeminarApplicationInfo seminarApplicationInfo) {
        if (!seminarApplicationReader.canEditOrDelete(seminarApplicationInfo.getId())) {
            throw new ServiceException(ErrorCode.SEMINAR_APPLICATION_CANNOT_EDIT_OR_DELETE);
        }
    }

    public void validateIsApplicationOwner(Long userId, Long applicationOwnerId) {
        if (!Objects.equals(userId, applicationOwnerId)) {
            throw new ServiceException(ErrorCode.SEMINAR_APPLICATION_ACCESS_DENIED);
        }
    }

    @Transactional
    public void deleteSeminarApplication(Long userId, Long seminarApplicationId) {
        SeminarApplicationInfo savedApplication = seminarApplicationReader.findById(seminarApplicationId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SEMINAR_APPLICATION_NOT_FOUND));

        this.validateCanEditOrDelete(savedApplication);
        this.validateIsApplicationOwner(userId, savedApplication.getUserTarget().getUserId());

        seminarApplicationWriter.deleteSeminarApplication(seminarApplicationId);
    }
}
