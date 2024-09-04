package com.example.demo.domain.study_project_application.domain.entity;

import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_project_applicants")
@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE study_project_applicants SET deleted_at = NOW() where id=?")
@SQLRestriction(value = "deleted_at is NULL")
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

    @OneToMany(mappedBy = "studyProjectApplicant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudyProjectApplicantAnswer> answerList = new ArrayList<>();

    @Builder
    public StudyProjectApplicant(StudyProjectBoard studyProjectBoard, User user) {
        this.studyProjectBoard = studyProjectBoard;
        this.user = user;
    }

    public static StudyProjectApplicant from(StudyProjectBoard studyProjectBoard, User user) {
        return StudyProjectApplicant.builder()
                .studyProjectBoard(studyProjectBoard)
                .user(user)
                .build();
    }
}
