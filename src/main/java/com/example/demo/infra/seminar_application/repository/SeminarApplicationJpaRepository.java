package com.example.demo.infra.seminar_application.repository;

import com.example.demo.infra.seminar_application.entity.SeminarApplication;
import com.example.demo.infra.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeminarApplicationJpaRepository extends JpaRepository<SeminarApplication, Long> {
    List<SeminarApplication> findByUser(User user);

    Page<SeminarApplication> findByUser(User user, Pageable pageable);
}
