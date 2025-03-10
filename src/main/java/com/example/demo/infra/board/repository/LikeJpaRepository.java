package com.example.demo.infra.board.repository;

import com.example.demo.infra.board.entity.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeJpaRepository extends JpaRepository<Like,Long> {

	@Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.board.id = :boardId AND l.user.id = :userId")
	Boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
