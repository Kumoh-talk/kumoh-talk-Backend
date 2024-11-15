package com.example.demo.domain.comment.domain.entity;


import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE comments SET deleted_at = NOW() where id = ?")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 500)
    @NotBlank(message = "댓글 내용은 빈 값일 수 없습니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_board_id")
    private RecruitmentBoard recruitmentBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Comment parentComment;

    // 쿼리 실험 필요
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Comment> replyComments = new ArrayList<>();

    private void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
        if (parentComment != null)
            parentComment.getReplyComments().add(this);
    }

    @Builder(builderMethodName = "seminarCommentBuilder", builderClassName = "SeminarCommentBuilder")
    public Comment(String content, Board board, User user, Comment parentComment) {
        this.content = content;
        this.board = board;
        this.user = user;
        setParentComment(parentComment);
    }

    @Builder(builderMethodName = "recruitmentCommentBuilder", builderClassName = "RecruitmentCommentBuilder")
    public Comment(String content, RecruitmentBoard recruitmentBoard, User user, Comment parentComment) {
        this.content = content;
        this.recruitmentBoard = recruitmentBoard;
        this.user = user;
        setParentComment(parentComment);
    }

    public static Comment fromSeminarBoardRequest(
            User user,
            Board board,
            CommentRequest commentRequest,
            Comment parentComment) {
        Comment comment = Comment.seminarCommentBuilder()
                .content(commentRequest.getContent())
                .board(board)
                .user(user)
                .build();
        comment.setParentComment(parentComment);

        return comment;
    }

    public static Comment fromRecruitmentBoardRequest(
            User user,
            RecruitmentBoard recruitmentBoard,
            CommentRequest commentRequest,
            Comment parentComment) {
        Comment comment = Comment.recruitmentCommentBuilder()
                .content(commentRequest.getContent())
                .recruitmentBoard(recruitmentBoard)
                .user(user)
                .build();
        comment.setParentComment(parentComment);

        return comment;
    }

    public void changeContent(String newContent) {
        this.content = newContent;
    }


}