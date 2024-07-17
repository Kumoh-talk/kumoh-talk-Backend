package com.example.demo.domain.board.domain.entity;

import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "like_board")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
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
        board.getLikes().add(this);
    }
}
