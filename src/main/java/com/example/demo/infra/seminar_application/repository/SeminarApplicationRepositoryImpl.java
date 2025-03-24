package com.example.demo.infra.seminar_application.repository;

import com.example.demo.domain.seminar_application.entity.SeminarApplicationInfo;
import com.example.demo.domain.seminar_application.repository.SeminarApplicationRepository;
import com.example.demo.infra.seminar_application.entity.SeminarApplication;
import com.example.demo.infra.user.entity.User;
import com.example.demo.infra.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeminarApplicationRepositoryImpl implements SeminarApplicationRepository {
    private final SeminarApplicationJpaRepository seminarApplicationJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<SeminarApplicationInfo> findById(Long id) {
        return seminarApplicationJpaRepository.findById(id).map(SeminarApplication::toDomain);
    }

    @Override
    public boolean isFirstApplication(Long userId) {
        User user = userJpaRepository.findById(userId).get();

        return seminarApplicationJpaRepository.findByUser(user).isEmpty();
    }

    @Override
    public void addSeminarApplication(Long userId, SeminarApplicationInfo seminarApplicationInfo) {
        User user = userJpaRepository.findById(userId).get();
        user.getSeminarApplications().add(SeminarApplication.from(seminarApplicationInfo, user));
    }

    @Override
    public void updateSeminarApplication(Long id, SeminarApplicationInfo seminarApplicationInfo) {
        seminarApplicationJpaRepository.findById(id).ifPresent(seminarApplication ->
                seminarApplication.updateSeminarApplicationInfo(seminarApplicationInfo));
    }

    @Override
    public boolean canEditOrDelete(Long id) {
        return seminarApplicationJpaRepository.findById(id).get().canEditOrDelete();
    }

    @Override
    public void deleteById(Long id) {
        seminarApplicationJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Page<SeminarApplicationInfo> findByUserId(Long userId, Pageable pageable) {
        User user = userJpaRepository.findById(userId).get();

        return seminarApplicationJpaRepository.findByUser(user, pageable).map(SeminarApplication::toDomain);
    }

    @Override
    public Page<SeminarApplicationInfo> findAll(Pageable pageable) {
        return seminarApplicationJpaRepository.findAll(pageable).map(SeminarApplication::toDomain);
    }
}
