package com.example.demo.domain.comment.domain.entity;


import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name ="comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() where id = ?")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 45)
    @NotBlank(message = "해당 내용은 빈 값일 수 없습니다.")
    private String content;

    // 지연로딩 즉시로딩 고민
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="board_id",nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="parent_id")
    private Comment parentComment = null;

    // 쿼리 실험 필요 
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parentComment")
    private Set<Comment> replyComments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "comment_like",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> likedUsers = new ArrayList<>();

    public Comment(String content, Board board, User user, Comment parentComment) {
        this.content = content;
        this.board = board;
        this.user = user;
        this.parentComment = parentComment;
    }

    public static CommentInfoResponse newCommentInfoResponse(Comment comment) {
        return new CommentInfoResponse(comment.getId(), comment.getUser().getName(), comment.getContent());
    }
    public void changeContent(String newContent){
        this.content = newContent;
    }
}