package com.example.demo.infra.board.entity;

import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "likes")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE likes SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
@EntityListeners(LikeEntityListener.class)
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Like(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}
