package com.example.demo.domain.application.reposiotry;

import com.example.demo.domain.application.domain.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}
