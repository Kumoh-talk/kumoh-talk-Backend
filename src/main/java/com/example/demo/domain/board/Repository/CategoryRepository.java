package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByName(String name);
}
