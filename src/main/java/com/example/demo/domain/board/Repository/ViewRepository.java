package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ViewRepository extends JpaRepository<View,Long> {
    @Transactional
    @Modifying
    @Query("UPDATE View v SET v.deletedAt = NOW() WHERE v.board.id = :boardId")
    void deleteByBoardId(Long boardId);
}
