package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b JOIN FETCH b.comments q WHERE b.id = :id")
    Optional<Board> findPostByIdWithComments(@Param("id") Long id);

    @Query("SELECT COUNT(v) FROM View v WHERE v.board.id = :boardId")
    long countViewsByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.board.id = :boardId")
    long countLikesByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT c.name FROM Board b " +
            "left join b.boardCategories bc on b.id = bc.board.id " +
            "left join bc.category c on bc.category.id = c.id " +
            "where b.id = :id")
    List<String> findCategoryNameByBoardId(@Param("id") Long id);

}

