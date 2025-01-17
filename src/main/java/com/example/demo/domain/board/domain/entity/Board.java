package com.example.demo.domain.board.domain.entity;


import com.example.demo.domain.board.domain.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.domain.dto.request.BoardUpdateRequest;
import com.example.demo.domain.board.domain.dto.vo.BoardType;
import com.example.demo.domain.board.domain.dto.vo.Status;
import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Column(nullable = false,length = 30)
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @Lob
    @Column(nullable = false)
    @NotBlank(message = "본문은 빈 값일 수 없습니다.")
    private String content;

    @Column(nullable = true,length = 500)
    private String attachFileUrl;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시판 타입은 빈 값일 수 없습니다.")
    private BoardType boardType; //TODO : 각 종 조회에 세미나 와 공지사항 분리하도록 쿼리 추가해야하는지 확인 필요

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시판 상태는 빈 값일 수 없습니다.")
    private Status status;

    @Column(nullable = false,length = 500)
    @NotBlank(message = "게시물 헤드 이미지는 빈 값일 수 없습니다.")
    private String headImageUrl;

    @Column(nullable = false)
    private Long viewCount;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ImageFile> imageFiles = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Like> likes= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BoardCategory> boardCategories = new ArrayList<>();


    private Board(String title, String content, User user, BoardType boardType,Status status,String headImageUrl) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.boardType = boardType;
        this.status = status;
        this.attachFileUrl = null;
        this.headImageUrl = headImageUrl;
        this.viewCount = 0L;
    }

    public static Board fromBoardRequest(User user, BoardCreateRequest boardCreateRequest){
        Board board = new Board(boardCreateRequest.getTitle(),
            boardCreateRequest.getContents(),
            user,
            boardCreateRequest.getBoardType(),
            Status.DRAFT,
            boardCreateRequest.getBoardHeadImageUrl());
        user.getBoards().add(board);
        return board;
    }

    public void changeBoardInfo(BoardUpdateRequest boardUpdateRequest){
        this.title = boardUpdateRequest.getTitle();
        this.content = boardUpdateRequest.getContents();
    }

    public void publishBoard(){
        this.status = Status.PUBLISHED;
    }

    public void changeAttachFileUrl(String attachFileUrl){
        this.attachFileUrl = attachFileUrl;
    }

    public void changeHeadImageUrl(String boardHeadImageUrl) {
        this.headImageUrl = boardHeadImageUrl;
    }
}
