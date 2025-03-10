package com.example.demo.infra.board.repository;

import com.example.demo.infra.board.entity.Board;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.repository.CommonBoardRepository;
import com.example.demo.infra.board.querydsl.BoardDslRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long>, CommonBoardRepository, BoardDslRepository {
    @Query("SELECT COUNT(l) FROM Like l WHERE l.board.id = :boardId")
    long countLikesByBoardId(@Param("boardId") Long boardId);

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :boardId")
    void increaseViewCount(@Param("boardId") Long boardId);

    @Override
    @Transactional(readOnly = true)
    default Optional<GenericBoard> doFindById(Long id) {
        Optional<Board> board = findById(id);
        if (board.isPresent()) {
            return Optional.of(board.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.user " +
            "WHERE b.id = :id")
    Optional<GenericBoard> findByIdWithUser(Long id);
}

