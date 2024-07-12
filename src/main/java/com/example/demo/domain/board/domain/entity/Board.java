package com.example.demo.domain.board.domain.entity;


import com.example.demo.domain.board.domain.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.vo.Status;
import com.example.demo.domain.board.domain.vo.Tag;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="board")
@NoArgsConstructor
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "게시물은 빈 값일 수 없습니다.")
    private String content;


    @Column(nullable = false,length = 15)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시물 분류는 빈 값일 수 없습니다")
    private Tag tag;

    @Column(nullable = false,length = 15)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시물 상태는 빈 값일 수 없습니다")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<File> files= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<Like> likes= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<BoardCategory> boardCategories = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    private List<View> views = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, User user, Tag tag,Status status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.tag = tag;
        this.status = status;
    }

    public static Board fromBoardRequest(User user, BoardCreateRequest boardCreateRequest){
        return Board.builder()
                .title(boardCreateRequest.getTitle())
                .content(boardCreateRequest.getContents())
                .user(user)
                .tag(boardCreateRequest.getTag())
                .status(Status.DRAFT)
                .build();
    }

    public void changeBoardStatus(Status status){
        this.status = status;
    }


}
