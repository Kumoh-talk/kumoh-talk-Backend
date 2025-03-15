package com.example.demo.infra.comment.entity;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.report.domain.Report;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.recruitment_board.entity.CommentBoard;
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
@Table(name = "board_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE board_comments SET deleted_at = NOW() where id = ?")
//@EntityListeners(CommentEntityListener.class)
public class BoardComment extends BaseEntity implements Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "content", nullable = false, length = 500)
    @NotBlank(message = "댓글 내용은 빈 값일 수 없습니다.")
    protected String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private BoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<BoardComment> replyComments = new ArrayList<>();

    @Builder
    public BoardComment(String content, Board board, User user, BoardComment parentComment) {
        this.content = content;
        this.board = board;
        this.user = user;
        this.parentComment = parentComment;
    }

    public static BoardComment of(CommentInfo commentInfo, User commentUser, CommentBoard commentBoard, Comment parentComment) {
        return BoardComment.builder()
                .content(commentInfo.getContent())
                .user(commentUser)
                .board((Board) commentBoard)
                .parentComment((BoardComment) parentComment)
                .build();
    }

    public List<Comment> getReplyComments() {
        return new ArrayList<>(replyComments);
    }

    @Override
    public void changeContent(String newContent) {
        this.content = newContent;
    }

    @Override
    public Report toReport(User user) {
        return Report.fromBoardComment(user, this);
    }

    @Override
    public CommentInfo toCommentInfoDomain() {
        return CommentInfo.builder()
                .commentId(id)
                .groupId(parentComment == null ? null : parentComment.getId())
                .commentUserInfo(CommentUserInfo.from(user))
                .content(content)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .deletedAt(getDeletedAt())
                .replyComments(replyComments.stream()
                        .map(BoardComment::toCommentInfoDomain)
                        .toList())
                .boardId(board.getId())
                .build();
    }

    @Override
    public MyCommentInfo toMyCommentInfoDomain() {
        return MyCommentInfo.builder()
                .commentId(id)
                .commentContent(content)
                .commentCreatedAt(getCreatedAt())
                .commentUpdatedAt(getUpdatedAt())
                .boardId(board.getId())
                .boardTitle(board.getTitle())
                .boardCreatedAt(board.getCreatedAt())
                .boardUpdatedAt(board.getUpdatedAt())
                .build();
    }
}
