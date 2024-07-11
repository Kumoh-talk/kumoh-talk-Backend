package com.example.demo.domain.board.domain.entity;

import com.example.demo.domain.board.domain.ApplicationBoardCategory;
import com.example.demo.domain.board.domain.ApplicationBoardTag;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "applicationBoard")
@NoArgsConstructor
@Getter
public class ApplicationBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Enumerated(value = EnumType.STRING)
    private ApplicationBoardCategory category;

    @Enumerated(value = EnumType.STRING)
    private ApplicationBoardTag tag;

    @Column(nullable = false, length = 50)
    private String subjects;

    @Column(nullable = false)
    private int recruitment_num;

    private LocalDateTime period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
