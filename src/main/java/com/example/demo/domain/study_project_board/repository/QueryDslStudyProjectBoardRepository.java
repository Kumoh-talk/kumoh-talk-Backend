package com.example.demo.domain.study_project_board.repository;

import com.example.demo.domain.study_project_board.domain.dto.vo.StudyProjectBoardCategory;
import com.example.demo.domain.study_project_board.domain.entity.StudyProjectBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface QueryDslStudyProjectBoardRepository {
    List<StudyProjectBoard> findPublishedPageByNoOffset(int size, StudyProjectBoard lastBoard, StudyProjectBoardCategory category, boolean isFirst);

    Page<StudyProjectBoard> findPublishedPageByPageNum(Pageable pageable, StudyProjectBoardCategory category);

    Page<StudyProjectBoard> findPublishedPageByUserIdByPageNum(Long userId, Pageable pageable, StudyProjectBoardCategory category);

    List<StudyProjectBoard> findDraftPageByUserIdByNoOffset(Long userId, int size, Long lastBoardId, boolean isFirst);

    Optional<StudyProjectBoard> findByIdByFetchingUser(Long ApplicationBoardId);
}
