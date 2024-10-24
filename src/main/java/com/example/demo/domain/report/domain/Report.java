package com.example.demo.domain.report.domain;

import com.example.demo.domain.comment.domain.entity.Comment;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 단방향
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // 단방향
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Builder
    public Report(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }

    public static Report from(User user, Comment comment) {
        return new Report(user, comment);
    }
}
