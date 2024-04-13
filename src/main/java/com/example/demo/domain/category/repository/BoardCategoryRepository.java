package com.example.demo.domain.category.repository;

import com.example.demo.domain.category.domain.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Long> {
}
