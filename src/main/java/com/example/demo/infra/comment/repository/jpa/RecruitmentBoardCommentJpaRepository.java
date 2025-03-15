package com.example.demo.infra.comment.repository.jpa;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.comment.entity.Comment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.recruitment_board.entity.CommentBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentBoardCommentJpaRepository extends JpaRepository<RecruitmentBoardComment, Long>, CommentJpaRepository<RecruitmentBoardType> {
    @Override
    default Optional<Comment> doFindById(Long id) {
        Optional<RecruitmentBoardComment> recruitmentBoardComment = findById(id);
        if (recruitmentBoardComment.isPresent()) {
            return Optional.of(recruitmentBoardComment.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    default Comment doSave(CommentInfo commentInfo, User commentUser, CommentBoard commentBoard, Comment parentComment) {
        return save(RecruitmentBoardComment.of(commentInfo, commentUser, commentBoard, parentComment));
    }

    @Override
    default void doDelete(Comment comment) {
        delete((RecruitmentBoardComment) comment);
    }

    @Query("SELECT rbc FROM RecruitmentBoardComment rbc " +
            "WHERE rbc.id = :id " +
            "AND rbc.board.id = :boardId")
    List<Comment> findByIdAndBoardId(Long id, Long boardId);

    @Override
    @Query("SELECT rbc FROM RecruitmentBoardComment rbc " +
            "JOIN FETCH rbc.user u " +
            "LEFT JOIN FETCH rbc.replyComments rc " +
            "WHERE rbc.board.id = :recruitmentBoardId AND rbc.parentComment IS NULL " +
            "ORDER BY rbc.createdAt ASC")
    List<Comment> findListByBoardId(Long recruitmentBoardId);


    @Override
    @Query("SELECT rbc FROM RecruitmentBoardComment rbc " +
            "JOIN FETCH rbc.board b " +
            "WHERE rbc.board IS NOT NULL " +
            "AND b.type = :recruitmentBoardType " +
            "AND rbc.user.id = :userId " +
            "AND rbc.deletedAt IS NULL " +
            "ORDER BY rbc.createdAt DESC")
    Page<Comment> findPageByUserId(Long userId, Pageable pageable, RecruitmentBoardType recruitmentBoardType);

    @Override
    @Query("SELECT rbc FROM RecruitmentBoardComment rbc " +
            "JOIN rbc.board b " +
            "WHERE rbc.id = :commentId " +
            "AND b.id = :boardId " +
            "AND rbc.deletedAt IS NULL")
    Optional<Comment> findNotDeleteCommentById(Long boardId, Long commentId);

    @Override
    @Query("SELECT DISTINCT rbc.user FROM RecruitmentBoardComment rbc " +
            "WHERE rbc.board.id = :boardId " +
            "AND (:parentCommentId IS NULL AND rbc.parentComment IS NULL) " +
            "OR (rbc.parentComment.id = :parentCommentId OR rbc.id = :parentCommentId) " +
            "AND rbc.deletedAt IS NULL")
    List<User> findUsersByBoardIdByParentCommentId(Long boardId, Long parentCommentId);
}
