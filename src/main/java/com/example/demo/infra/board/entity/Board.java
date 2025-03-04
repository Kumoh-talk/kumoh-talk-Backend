package com.example.demo.infra.board.entity;


import com.example.demo.application.board.dto.request.BoardCreateRequest;
import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.board.service.entity.vo.Status;
import com.example.demo.domain.board.service.entity.BoardCategoryNames;
import com.example.demo.domain.board.service.entity.BoardContent;
import com.example.demo.domain.board.service.entity.BoardInfo;
import com.example.demo.domain.comment.domain.entity.BoardComment;
import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserTarget;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.board.category.entity.BoardCategory;

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
@Table(name = "boards")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE boards SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class Board extends BaseEntity implements GenericBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @Lob
    @Column(nullable = false)
    @NotBlank(message = "본문은 빈 값일 수 없습니다.")
    private String content;

    @Column(nullable = true, length = 500)
    private String attachFileUrl;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시판 타입은 빈 값일 수 없습니다.")
    private BoardType boardType; //TODO : 각 종 조회에 세미나 와 공지사항 분리하도록 쿼리 추가해야하는지 확인 필요

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "게시판 상태는 빈 값일 수 없습니다.")
    private Status status;

    @Column(nullable = true, length = 500)
    private String headImageUrl;

    @Column(nullable = false)
    private Long viewCount;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoardComment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoardCategory> boardCategories = new ArrayList<>();

    @Builder
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

    public Board(Long id) {
        this.id = id;
    }

    public void changeBoardInfo(BoardContent boardContent){
        this.title = boardContent.getTitle();
        this.content = boardContent.getContents();
        this.status = boardContent.getBoardStatus();
        this.headImageUrl = boardContent.getBoardHeadImageUrl();

    }

    public void changeAttachFileUrl(String attachFileUrl){
        this.attachFileUrl = attachFileUrl;
    }

    public static BoardInfo toBoardInfo(Board board,Long likeCount) {
        BoardContent boardContent = new BoardContent(board.getTitle(), board.getContent(), board.getBoardType(), board.getHeadImageUrl(),board.status);
        UserTarget userTarget = UserTarget.builder()
            .userId(board.getUser().getId())
            .nickName(board.getUser().getNickname())
            .userRole(board.getUser().getRole())
            .build();
        List<String> categoryNames = new ArrayList<>();
        for (BoardCategory boardCategory : board.getBoardCategories()) {
            categoryNames.add(boardCategory.getCategory().getName());
        }
        BoardCategoryNames boardCategoryNames = new BoardCategoryNames(categoryNames);
        return BoardInfo.builder()
            .boardId(board.getId())
            .boardContent(boardContent)
            .viewCount(board.getViewCount())
            .likeCount(likeCount)
            .createdAt(board.getCreatedAt())
            .updatedAt(board.getUpdatedAt())
            .userTarget(userTarget)
            .boardCategoryNames(boardCategoryNames)
            .build();
    }
}
