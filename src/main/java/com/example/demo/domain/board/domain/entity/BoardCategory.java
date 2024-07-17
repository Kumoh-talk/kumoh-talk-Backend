package com.example.demo.domain.board.domain.entity;


import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name ="board_category")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
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
        category.getBoardCategories().add(this);
    }
}
