package com.example.demo.infra.board.category.repository;

import java.util.Optional;

import com.example.demo.infra.board.category.entity.BoardCategory;
import com.example.demo.infra.board.category.querydsl.BoardCategoryDslRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardCategoryJpaRepository extends JpaRepository<BoardCategory,Long>, BoardCategoryDslRepository {
    @Query("SELECT COUNT(bc) FROM BoardCategory bc WHERE bc.category.id = :categoryId")
    Long countBoardCategoryByCategoryId(Long categoryId);

    Optional<BoardCategory> findByNameAndBoardId(String categoryName, Long id);
}
