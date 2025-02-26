package com.example.demo.infra.board.Repository;

import com.example.demo.infra.board.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardCategoryJpaRepository extends JpaRepository<BoardCategory,Long> {


    @Query("SELECT COUNT(bc) FROM BoardCategory bc WHERE bc.category.id = :categoryId")
    Long countBoardCategoryByCategoryId(Long categoryId);

    Optional<BoardCategory> findByName(String name);

    Optional<BoardCategory> findByNameAndBoardId(String categoryName, Long id);
}
