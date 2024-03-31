package com.example.demo.domain.board.domain;


import com.example.demo.domain.category.domain.entity.BoardCategory;
import com.example.demo.domain.comment.domain.Comment;
import com.example.demo.domain.file.domain.entity.File;
import com.example.demo.domain.like.domain.Like;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    @NotBlank(message = "조회수는 빈 값일 수 없습니다.")
    private Long view;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // 파일 엔티티
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;


    @OneToMany(mappedBy = "board",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)// TODO: 삭제 전이 한번 다시 볼 필요 있음
    private List<BoardCategory> boardCategories;

    @Builder
    public Board(String title, String content, Long view, User user) {
        this.title = title;
        this.content = content;
        this.view = view;
        this.user = user;
    }


}
