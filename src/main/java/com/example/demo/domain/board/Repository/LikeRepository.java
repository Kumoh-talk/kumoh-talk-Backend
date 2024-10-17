package com.example.demo.domain.board.Repository;

import java.util.Optional;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.entity.Like;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like,Long> {

	@Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.board.id = :boardId AND l.user.id = :userId")
	Boolean existsByBoardIdAndUserId(Long boardId, Long userId);

	Optional<Like> findByBoardIdAndUserId(Long boardId, Long userId);

	@Query("SELECT new com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse"
		+ "(b.id, b.title, b.user.nickname, b.tag, COUNT(DISTINCT v.id), COUNT(DISTINCT l.id), b.createdAt) "
		+ "FROM Board b "
		+ "JOIN b.likes l "
		+ "LEFT JOIN b.views v "
		+ "WHERE l.user.id = :userId "
		+ "GROUP BY b.id, b.title, b.user.nickname, b.tag, b.createdAt")
	Page<BoardTitleInfoResponse> findBoardsByUserId(Long userId, Pageable pageable);
}
