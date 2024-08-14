package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Long> {


    @Query("SELECT COUNT(bc) FROM BoardCategory bc WHERE bc.category.id = :categoryId")
    long countBoardCategoryByCategoryId(Long categoryId);
}
