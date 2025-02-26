package com.example.demo.infra.board.Repository;

import java.util.Optional;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.infra.board.entity.Like;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like,Long> {

	@Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.board.id = :boardId AND l.user.id = :userId")
	Boolean existsByBoardIdAndUserId(Long boardId, Long userId);

	Optional<Like> findByBoardIdAndUserId(Long boardId, Long userId);

	@Query("SELECT new com.example.demo.domain.board.service.entity.BoardTitleInfo"
		+ "(b.id, b.title, b.user.nickname, b.boardType, b.viewCount, COUNT(DISTINCT l.id),b.headImageUrl, b.createdAt) "
		+ "FROM Board b "
		+ "JOIN b.likes l "
		+ "WHERE l.user.id = :userId "
		+ "GROUP BY b.id, b.title, b.user.nickname, b.boardType, b.createdAt")
	Page<BoardTitleInfo> findBoardsByUserId(Long userId, Pageable pageable);
}
