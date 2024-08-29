package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardCategory;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyProjectBoardRepository extends JpaRepository<StudyProjectBoard, Long>, QueryDslStudyProjectBoardRepository {
    @Query("Select sb.id From StudyProjectBoard sb " +
            "where sb.recruitmentDeadline >= CURRENT_TIMESTAMP " +
            "and sb.status = com.example.demo.domain.board.domain.dto.vo.Status.PUBLISHED " +
            "and sb.category = :boardCategory " +
            "order by sb.recruitmentDeadline asc")
    Optional<List<Long>> findPublishedId(StudyProjectBoardCategory boardCategory);

    @Query("Select Max(id) From StudyProjectBoard " +
            "where status = com.example.demo.domain.board.domain.dto.vo.Status.DRAFT " +
            "and user.id = :userId")
    Optional<Long> findFirstDraftIdByUserId(Long userId);
}
