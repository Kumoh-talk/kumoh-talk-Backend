package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse;
import com.example.demo.domain.board.domain.dto.vo.BoardType;
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

    @Query("SELECT COUNT(l) FROM Like l WHERE l.board.id = :boardId")
    long countLikesByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT c.name FROM Board b " +
            "left join b.boardCategories bc on b.id = bc.board.id " +
            "left join bc.category c on bc.category.id = c.id " +
            "where b.id = :id")
    List<String> findCategoryNameByBoardId(@Param("id") Long id);

    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse"
        + "(b.id, b.title, b.user.nickname, b.boardType, b.viewCount, COUNT(DISTINCT l),b.headImageUrl ,b.createdAt) "
        + "FROM Board b "
        + "LEFT JOIN b.likes l "
        + "WHERE b.status = 'PUBLISHED' AND b.boardType = :boardType "
        + "GROUP BY b.id, b.title, b.user.nickname, b.boardType, b.createdAt")
    Page<BoardTitleInfoResponse> findBoardByPage(@Param("boardType") BoardType boardType, Pageable pageable);//TODO : 추후 QueryDSL로 변경


    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse "
    + "(b.id, b.title, b.createdAt, b.updatedAt) "
    + "FROM Board b "
    + "WHERE b.user.id = :userId AND b.status = 'DRAFT'")
    Page<DraftBoardTitleResponse> findDraftBoardByPage(Long userId, Pageable pageable);

    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse"
    + "(b.id, b.title, b.user.nickname, b.boardType,b.viewCount, COUNT(DISTINCT l),b.headImageUrl ,b.createdAt) "
    + "FROM Board b "
    + "LEFT JOIN b.likes l "
    + "WHERE b.status = 'PUBLISHED' AND b.user.id = :userId AND b.boardType = :boardType "
    + "GROUP BY b.id, b.title, b.user.nickname, b.boardType, b.createdAt")
    Page<BoardTitleInfoResponse> findPublishedBoardListByUser(@Param("userId") Long userId,
        @Param("boardType") BoardType boardType,
        Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + 1 WHERE b.id = :boardId")
    void increaseViewCount(@Param("boardId") Long boardId);

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + :viewCount WHERE b.id = :boardId")
    void increaseViewCount(@Param("boardId") Long boardId, @Param("viewCount") int viewCount);

}

