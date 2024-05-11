package com.example.demo.domain.comment.domain.entity;


import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="group_id")
    private Comment parentComment;

    @Column(name = "depth", nullable = false)
    private int depth;

    // 쿼리 실험 필요
    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private Set<Comment> replyComments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "comment_like",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> likedUsers = new ArrayList<>();

    @Builder
    public Comment(String content, Board board, User user, int depth) {
        this.content = content;
        this.board = board;
        this.user = user;
        this.depth = depth;
    }

    public void changeContent(String newContent){
        this.content = newContent;
    }
}