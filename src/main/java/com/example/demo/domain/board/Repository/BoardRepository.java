package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse;
import com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse;
import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.repository.CommonBoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, CommonBoardRepository {
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
            + "(b.id, b.title, b.user.nickname, b.boardType, COUNT(DISTINCT v), COUNT(DISTINCT l),b.headImageUrl ,b.createdAt) "
            + "FROM Board b "
            + "LEFT JOIN b.likes l "
            + "LEFT JOIN b.views v "
            + "WHERE b.status = 'PUBLISHED' AND b.boardType = :boardType "
            + "GROUP BY b.id, b.title, b.user.nickname, b.boardType, b.createdAt")
    Page<BoardTitleInfoResponse> findBoardByPage(@Param("boardType") BoardType boardType, Pageable pageable);//TODO : 추후 QueryDSL로 변경

    @Transactional
    @Modifying
    @Query("UPDATE ImageFile f SET f.deletedAt = NOW() WHERE f.board.id = :boardId")
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

    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.DraftBoardTitleResponse "
            + "(b.id, b.title, b.createdAt, b.updatedAt) "
            + "FROM Board b "
            + "WHERE b.user.id = :userId AND b.status = 'DRAFT'")
    Page<DraftBoardTitleResponse> findDraftBoardByPage(Long userId, Pageable pageable);

    @Query("SELECT new com.example.demo.domain.board.domain.dto.response.BoardTitleInfoResponse"
            + "(b.id, b.title, b.user.nickname, b.boardType, COUNT(DISTINCT v), COUNT(DISTINCT l),b.headImageUrl ,b.createdAt) "
            + "FROM Board b "
            + "LEFT JOIN b.likes l "
            + "LEFT JOIN b.views v "
            + "WHERE b.status = 'PUBLISHED' AND b.user.id = :userId AND b.boardType = :boardType "
            + "GROUP BY b.id, b.title, b.user.nickname, b.boardType, b.createdAt")
    Page<BoardTitleInfoResponse> findPublishedBoardListByUser(@Param("userId") Long userId,
                                                              @Param("boardType") BoardType boardType,
                                                              Pageable pageable);

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

