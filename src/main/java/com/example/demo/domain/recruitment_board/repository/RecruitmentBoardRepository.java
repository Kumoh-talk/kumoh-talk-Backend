package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentBoardRepository extends JpaRepository<RecruitmentBoard, Long>, QueryDslRecruitmentBoardRepository {
    @Query("Select sb.id From RecruitmentBoard sb " +
            "where sb.recruitmentDeadline >= CURRENT_TIMESTAMP " +
            "and sb.status = com.example.demo.domain.board.domain.dto.vo.Status.PUBLISHED " +
            "and sb.type = :boardType " +
            "order by sb.recruitmentDeadline asc")
    Optional<List<Long>> findPublishedId(RecruitmentBoardType boardType);

    @Query("Select Max(id) From RecruitmentBoard " +
            "where status = com.example.demo.domain.board.domain.dto.vo.Status.DRAFT " +
            "and user.id = :userId")
    Optional<Long> findFirstDraftIdByUserId(Long userId);
}
