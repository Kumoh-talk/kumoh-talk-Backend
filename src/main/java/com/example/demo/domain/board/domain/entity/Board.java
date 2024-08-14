package com.example.demo.domain.board.domain.entity;


import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.board.domain.dto.vo.Tag;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="boards")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE boards SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
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
    private Tag tag; //TODO : 각 종 조회에 세미나 와 공지사항 분리하도록 쿼리 추가해야하는지 확인 필요

    @Column(nullable = false,length = 15)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
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
        Board board = Board.builder()
                .title(boardCreateRequest.getTitle())
                .content(boardCreateRequest.getContents())
                .user(user)
                .tag(boardCreateRequest.getTag())
                .status(Status.DRAFT)
                .build();
        user.getBoards().add(board);
        return board;
    }

    public void changeBoardInfo(BoardUpdateRequest boardUpdateRequest){
        this.title = boardUpdateRequest.getTitle();
        this.content = boardUpdateRequest.getContents();
    }

    public void changeBoardStatus(Status status){
        this.status = status;
    }


}
