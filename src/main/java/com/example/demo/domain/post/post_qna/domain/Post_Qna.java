package com.example.demo.domain.post.post_qna.domain;


import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="post_qna")
@NoArgsConstructor
@Getter
@Setter
public class Post_Qna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @Column(nullable = false,length = 500)
    @NotBlank(message = "게시물은 빈 값일 수 없습니다.")
    private String contents;
    @ManyToOne
    @JoinColumn(name ="post_id",nullable = false)
    private Post post;


    public Post_Qna(String title, String contents, Post post) {
        this.title = title;
        this.contents = contents;
        this.post = post;
    }
}
