package com.example.demo.domain.file.repository;

import com.example.demo.domain.file.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileRepository extends JpaRepository<File, Long> {
}
