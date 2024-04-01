package com.example.demo.domain.comment.domain;


import com.example.demo.domain.comment.domain.response.CommentInfoResponse;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.board.domain.Board;
import com.example.demo.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name ="comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "해당 내용은 빈 값일 수 없습니다.")
    private String content;

    @ManyToOne
    @JoinColumn(name ="post_id",nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user;



    public Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public static CommentInfoResponse entityToResponse(Comment comment) {
        return new CommentInfoResponse(comment.getId(), comment.getUser().getName(), comment.getContent());
    }
}
