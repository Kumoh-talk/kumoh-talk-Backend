package com.example.demo.domain.board.domain.entity;


import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="board_category")
@NoArgsConstructor
@Getter
@Setter
public class BoardCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;



    public BoardCategory(Board board, Category category) {
        this.board = board;
        this.category = category;
    }
}
