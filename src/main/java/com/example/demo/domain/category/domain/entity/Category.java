package com.example.demo.domain.category.domain.entity;

import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name ="board_category")
@NoArgsConstructor
@Getter
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 45)
    @NotBlank(message = "이름은 빈 값일 수 없습니다.")
    private String name;

    @OneToMany(mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true) // TODO: 삭제 전이 한번 다시 볼 필요 있음
    private List<BoardCategory> boardCategories;
}
