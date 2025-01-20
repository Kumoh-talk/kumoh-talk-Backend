package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.comment.domain.dto.response.MyCommentResponse;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.comment.domain.entity.RecruitmentBoardComment;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentBoardCommentRepository extends JpaRepository<RecruitmentBoardComment, Long>, CommonCommentRepository<RecruitmentBoardType> {
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
    default Comment doSave(User commentUser, GenericBoard commentBoard, CommentRequest commentRequest, Comment parentComment) {
        return save(RecruitmentBoardComment
                .fromRecruitmentBoardCommentRequest(
                        commentUser,
                        (RecruitmentBoard) commentBoard,
                        commentRequest,
                        (RecruitmentBoardComment) parentComment));
    }

    @Override
    default void doDelete(Comment comment) {
        delete((RecruitmentBoardComment) comment);
    }

    @Override
    @Query("SELECT rbc FROM RecruitmentBoardComment rbc JOIN FETCH rbc.user u " +
            "LEFT JOIN FETCH rbc.replyComments rc " +
            "WHERE rbc.board.id = :recruitmentBoardId AND rbc.parentComment IS NULL " +
            "ORDER BY rbc.createdAt ASC")
    List<Comment> findListByBoard_id(Long recruitmentBoardId);

    @Override
    default Page<MyCommentResponse> convertAndFindPageByUserId(Long userId, Pageable pageable, EntireBoardType entireBoardType) {
        return findPageByUser_id(userId, pageable, entireBoardType.getRecruitmentBoardType());
    }

    @Override
    @Query("SELECT new com.example.demo.domain.comment.domain.dto.response.MyCommentResponse " +
            "(rbc.id, rbc.content, rbc.createdAt, rbc.updatedAt, b.id, b.title, b.createdAt, b.updatedAt)" +
            "FROM RecruitmentBoardComment rbc " +
            "JOIN rbc.board b " +
            "WHERE rbc.board IS NOT NULL " +
            "AND b.type = :recruitmentBoardType " +
            "AND rbc.user.id = :userId " +
            "AND rbc.deletedAt IS NULL " +
            "ORDER BY rbc.createdAt DESC")
    Page<MyCommentResponse> findPageByUser_id(Long userId, Pageable pageable, RecruitmentBoardType recruitmentBoardType);

    @Override
    @Modifying
    @Query("UPDATE RecruitmentBoardComment rbc SET rbc.deletedAt = NOW() WHERE rbc.parentComment.id = :commentId")
    void softDeleteReplyCommentsById(Long commentId);

    @Override
    @Query("SELECT rbc FROM RecruitmentBoardComment rbc " +
            "JOIN rbc.board b " +
            "WHERE rbc.id = :commentId " +
            "AND b.id = :boardId " +
            "AND rbc.deletedAt IS NULL")
    Optional<Comment> findNotDeleteCommentById(Long boardId, Long commentId);
}

