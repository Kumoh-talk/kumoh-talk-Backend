package com.example.demo.domain.application.domain.entity;

import com.example.demo.domain.board.domain.entity.ApplicationBoard;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "applicationForm")
@NoArgsConstructor
@Getter
public class ApplicationForm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationBoard_id", nullable = false)
    private ApplicationBoard applicationBoard;
}
