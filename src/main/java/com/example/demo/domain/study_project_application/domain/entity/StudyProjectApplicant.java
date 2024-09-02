package com.example.demo.domain.study_project_application.domain.entity;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "study_project_applicants")
@NoArgsConstructor
@Getter
public class StudyProjectApplicant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_project_board_id", nullable = false)
    private StudyProjectBoard studyProjectBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public StudyProjectApplicant(StudyProjectBoard studyProjectBoard, User user) {
        this.studyProjectBoard = studyProjectBoard;
        this.user = user;
    }
}
