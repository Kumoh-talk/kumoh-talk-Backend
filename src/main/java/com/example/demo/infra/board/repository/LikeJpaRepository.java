package com.example.demo.infra.board.repository;

import com.example.demo.infra.board.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {

    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.board.id = :boardId AND l.user.id = :userId")
    Boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    @Query("SELECT l.id FROM Like l WHERE l.board.id = :boardId AND l.user.id = :userId")
    Optional<Long> findIdByBoardIdAndUserId(Long boardId, Long userId);
}
