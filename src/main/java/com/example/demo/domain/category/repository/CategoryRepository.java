package com.example.demo.domain.category.repository;

import com.example.demo.domain.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByName(String name);
}
