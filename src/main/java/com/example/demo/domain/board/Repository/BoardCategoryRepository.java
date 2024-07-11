package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Long> {
}
