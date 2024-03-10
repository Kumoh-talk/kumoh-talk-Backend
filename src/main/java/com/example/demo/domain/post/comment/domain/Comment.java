package com.example.demo.domain.post.comment.domain;


import com.example.demo.domain.post.domain.Post;
import com.example.demo.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String contents;

    @ManyToOne
    @JoinColumn(name ="user_id",nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name ="post_id",nullable = false)
    private Post post;


}
