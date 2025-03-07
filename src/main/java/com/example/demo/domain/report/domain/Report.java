package com.example.demo.domain.report.domain;

import com.example.demo.domain.comment.domain.entity.BoardComment;
import com.example.demo.domain.comment.domain.entity.RecruitmentBoardComment;
import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.user.entity.User;
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
    @JoinColumn(name = "board_comment_id", nullable = false)
    private BoardComment boardComment;

    @ManyToOne(fetch = FetchType.LAZY) // 단방향
    @JoinColumn(name = "recruitment_board_comment_id", nullable = false)
    private RecruitmentBoardComment recruitmentBoardComment;

    @Builder
    public Report(User user, BoardComment comment) {
        this.user = user;
        this.boardComment = comment;
    }

    public Report(User user, RecruitmentBoardComment comment) {
        this.user = user;
        this.recruitmentBoardComment = comment;
    }

    public static Report fromBoardComment(User user, BoardComment comment) {
        return new Report(user, comment);
    }

    public static Report fromRecruitmentBoardComment(User user, RecruitmentBoardComment comment) {
        return new Report(user, comment);
    }
}
