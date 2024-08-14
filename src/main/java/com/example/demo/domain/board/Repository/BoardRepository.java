package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.entity.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse"
        + "(b.id, b.title,b.user.nickname,b.tag,count(v) ,count(l), b.createdAt) "
        + "FROM Board b "
        + "left join Like l on b.id = l.board.id "
        + "left join View v on b.id = v.board.id ")
    Page<BoardTitleInfoResponse> findBoardByPage(Pageable pageable); //TODO : 추후 QueryDSL로 변경

    @Transactional
    @Modifying
    @Query("UPDATE File f SET f.deletedAt = NOW() WHERE f.board.id = :boardId")
    void deleteFileByBoardId(Long boardId);

    @Transactional
    @Modifying
    @Query("UPDATE Like l SET l.deletedAt = NOW() WHERE l.board.id = :boardId")
    void deleteLikeByBoardId(Long boardId);

    @Transactional
    @Modifying
    @Query("UPDATE View v SET v.deletedAt = NOW() WHERE v.board.id = :boardId")
    void deleteViewByBoardId(Long boardId);

    @Transactional
    @Modifying
    @Query("UPDATE BoardCategory bc SET bc.deletedAt = NOW() WHERE bc.board.id = :boardId")
    void deleteBoardCategoryByBoardId(Long boardId);

}

