package com.example.demo.domain.comment.repository;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.MyCommentResponse;
import com.example.demo.domain.comment.domain.entity.BoardComment;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.infra.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, CommonCommentRepository<BoardType> {
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
    default Comment doSave(User commentUser, GenericBoard commentBoard, CommentRequest commentRequest, Comment parentComment) {
        return save(BoardComment
                .fromBoardCommentRequest(
                        commentUser,
                        (Board) commentBoard,
                        commentRequest,
                        (BoardComment) parentComment));
    }

    @Override
    default void doDelete(Comment comment) {
        delete((BoardComment) comment);
    }

    @Override
    @Query("SELECT bc FROM BoardComment bc " +
            "JOIN FETCH bc.user u " +
            "LEFT JOIN FETCH bc.replyComments rc " +
            "WHERE bc.board.id = :boardId AND bc.parentComment IS NULL " +
            "ORDER BY bc.createdAt ASC")
    List<Comment> findListByBoard_id(Long boardId);

    @Override
    default Page<MyCommentResponse> convertAndFindPageByUserId(Long userId, Pageable pageable, EntireBoardType entireBoardType) {
        return findPageByUser_id(userId, pageable, entireBoardType.getBoardType());
    }

    @Override
    @Query("SELECT new com.example.demo.domain.comment.domain.dto.response.MyCommentResponse " +
            "(bc.id, bc.content, bc.createdAt, bc.updatedAt, b.id, b.title, b.createdAt, b.updatedAt)" +
            "FROM BoardComment bc " +
            "JOIN bc.board b " +
            "WHERE bc.board IS NOT NULL " +
            "AND b.boardType = :boardType " +
            "AND bc.user.id = :userId " +
            "AND bc.deletedAt IS NULL " +
            "ORDER BY bc.createdAt DESC")
    Page<MyCommentResponse> findPageByUser_id(Long userId, Pageable pageable, BoardType boardType);

    @Override
    @Modifying
    @Query("UPDATE BoardComment bc SET bc.deletedAt = NOW() WHERE bc.parentComment.id = :commentId")
    void softDeleteReplyCommentsById(Long commentId);

    @Override
    @Query("SELECT bc FROM BoardComment bc " +
            "JOIN bc.board b " +
            "WHERE bc.id = :commentId " +
            "AND b.id = :boardId " +
            "AND bc.deletedAt IS NULL")
    Optional<Comment> findNotDeleteCommentById(Long boardId, Long commentId);

    @Override
    @Query("SELECT DISTINCT bc.user FROM BoardComment bc " +
            "WHERE bc.board.id = :boardId " +
            "AND (:parentCommentId IS NULL AND bc.parentComment IS NULL OR (bc.parentComment.id = :parentCommentId OR bc.id = :parentCommentId)) " +
            "AND bc.deletedAt IS NULL")
    List<User> findUsersByBoard_idByParentComment_id(Long boardId, Long parentCommentId);
}
