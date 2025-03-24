package com.example.demo.infra.board.category.repository;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.infra.board.category.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

}
