package com.example.demo.infra.comment.repository.jpa;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.recruitment_board.domain.entity.CommentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardCommentJpaRepository extends JpaRepository<BoardComment, Long>, CommentJpaRepository<BoardType> {
    @Override
    default Optional<Comment> doFindById(Long id) {
        Optional<BoardComment> boardComment = findById(id);
        if (boardComment.isPresent()) {
            return Optional.of(boardComment.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    default Comment doSave(CommentInfo commentInfo, User commentUser, CommentBoard commentBoard, Comment parentComment) {
        return save(BoardComment.of(commentInfo, commentUser, commentBoard, parentComment));
    }

    @Override
    default void doDelete(Comment comment) {
        delete((BoardComment) comment);
    }

    @Query("SELECT bc FROM BoardComment bc " +
            "WHERE bc.id = :id " +
            "AND bc.board.id = :boardId")
    List<Comment> findByIdAndBoardId(Long id, Long boardId);

    @Query("SELECT bc FROM BoardComment bc " +
            "JOIN FETCH bc.user u " +
            "LEFT JOIN FETCH bc.replyComments rc " +
            "WHERE bc.board.id = :boardId AND bc.parentComment IS NULL " +
            "ORDER BY bc.createdAt ASC")
    List<Comment> findListByBoardId(Long boardId);

    @Query("SELECT bc FROM BoardComment bc " +
            "JOIN FETCH bc.board b " +
            "WHERE bc.board IS NOT NULL " +
            "AND b.boardType = :boardType " +
            "AND bc.user.id = :userId " +
            "AND bc.deletedAt IS NULL " +
            "ORDER BY bc.createdAt DESC")
    Page<Comment> findPageByUserId(Long userId, Pageable pageable, BoardType boardType);

    @Query("SELECT bc FROM BoardComment bc " +
            "JOIN bc.board b " +
            "WHERE bc.id = :commentId " +
            "AND b.id = :boardId " +
            "AND bc.deletedAt IS NULL")
    Optional<Comment> findNotDeleteCommentById(Long boardId, Long commentId);

    @Query("SELECT DISTINCT bc.user FROM BoardComment bc " +
            "WHERE bc.board.id = :boardId " +
            "AND (:parentCommentId IS NULL AND bc.parentComment IS NULL OR (bc.parentComment.id = :parentCommentId OR bc.id = :parentCommentId)) " +
            "AND bc.deletedAt IS NULL")
    List<User> findUsersByBoardIdByParentCommentId(Long boardId, Long parentCommentId);
}
