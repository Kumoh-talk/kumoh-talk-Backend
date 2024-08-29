package com.example.demo.domain.comment.domain.entity;


import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.comment.domain.request.CommentRequest;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
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

    @Column(name = "content", nullable = false, length = 200)
    @NotBlank(message = "해당 내용은 빈 값일 수 없습니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_board_id")
    private StudyProjectBoard studyProjectBoard;

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

    @Builder(builderMethodName = "studyProjectCommentBuilder", builderClassName = "StudyProjectCommentBuilder")
    public Comment(String content, StudyProjectBoard studyProjectBoard, User user, Comment parentComment) {
        this.content = content;
        this.studyProjectBoard = studyProjectBoard;
        this.user = user;
        setParentComment(parentComment);
    }

    public static Comment fromSeminarBoardRequest(CommentRequest request,
                                                  Board board, User user, Comment parentComment) {
        Comment comment = Comment.seminarCommentBuilder()
                .content(request.getContent())
                .board(board)
                .user(user)
                .build();
        comment.setParentComment(parentComment);

        return comment;
    }

    public static Comment fromStudyProjectBoardRequest(CommentRequest request,
                                                       StudyProjectBoard studyProjectBoard, User user, Comment parentComment) {
        Comment comment = Comment.studyProjectCommentBuilder()
                .content(request.getContent())
                .studyProjectBoard(studyProjectBoard)
                .user(user)
                .build();
        comment.setParentComment(parentComment);

        return comment;
    }

    public void changeContent(String newContent) {
        this.content = newContent;
    }


}