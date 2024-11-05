package com.example.demo.domain.board.domain.entity;

import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="categories")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE categories SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    @NotBlank(message = "이름은 빈 값일 수 없습니다.")
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BoardCategory> boardCategories = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
