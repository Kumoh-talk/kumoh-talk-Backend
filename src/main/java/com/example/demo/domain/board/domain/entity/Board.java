package com.example.demo.domain.board.domain.entity;


import com.example.demo.domain.board.domain.BoardStatus;
import com.example.demo.domain.category.domain.entity.BoardCategory;
import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.like.domain.Like;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="board")
@NoArgsConstructor
@Getter
@Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @Column(nullable = false,length = 500) //TODO : 내용 글자 수 제한 나중에 변경
    @NotBlank(message = "게시물은 빈 값일 수 없습니다.")
    private String content;


    @Column(nullable = false,length = 15)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시물 상태는 빈 값일 수 없음")
    private BoardStatus status;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> comments= new ArrayList<>();

    // 파일 엔티티
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true,fetch = FetchType.LAZY) // 연관관계 board만 맺고 있음
    private List<File> files= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Like> likes= new ArrayList<>();





    @OneToMany(mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)// TODO: 삭제 전이 한번 다시 볼 필요 있음
    private List<BoardCategory> boardCategories = new ArrayList<>(); // TODO : 카테고리 게시판 저장시 요청을 한번에 받는지 따로 카테고리만 연관하는 요청을 보내는지 확인 필요

    @Builder
    public Board(String title, String content, User user,BoardStatus status) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.status = status;
    }


}
