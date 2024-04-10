package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.Board;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b JOIN FETCH b.comments q WHERE b.id = :id")
    Optional<Board> findPostByIdWithComments(@Param("id") Long id);

    @Query("SELECT COUNT(v) FROM View v WHERE v.board.id = :boardId")
    long countViewsByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.board.id = :boardId")
    long countLikesByBoardId(@Param("boardId") Long boardId);

}

