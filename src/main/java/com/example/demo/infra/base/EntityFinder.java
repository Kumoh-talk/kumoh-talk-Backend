package com.example.demo.infra.base;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityFinder {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserJpaRepository userJpaRepository;

    public <K> K findById(Class<K> entityClass, Long id) {
        return entityManager.find(entityClass, id);
    }

    public User findUserById(Long userId) {
        User user = findById(User.class, userId);
        return user != null ? user : userJpaRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
    }
}
