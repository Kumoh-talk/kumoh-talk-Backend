package com.example.demo.infra.board.category.entity;


import com.example.demo.global.base.domain.BaseEntity;
import com.example.demo.infra.board.entity.Board;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name ="board_categories")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE board_categories SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class BoardCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public BoardCategory(Board board, Category category) {
        this.board = board;
        this.category = category;
        board.getBoardCategories().add(this);
        category.getBoardCategories().add(this);
    }

    public void removeAssosiationBoardAndCategory() {
        this.board.getBoardCategories().remove(this);
        this.category.getBoardCategories().remove(this);
        this.board = null;
        this.category = null;
    }
}
