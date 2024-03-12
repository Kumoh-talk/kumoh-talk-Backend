package com.example.demo.domain.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.File;

public interface FileRepository extends JpaRepository<File, Long> {
}
