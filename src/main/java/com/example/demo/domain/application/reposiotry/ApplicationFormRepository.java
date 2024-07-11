package com.example.demo.domain.application.reposiotry;

import com.example.demo.domain.application.domain.entity.ApplicationForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {
}
