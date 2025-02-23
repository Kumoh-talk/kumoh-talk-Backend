package com.example.demo.domain.recruitment_board.repository;

import com.example.demo.domain.recruitment_board.domain.entity.GenericBoard;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RecruitmentBoardRepository extends JpaRepository<RecruitmentBoard, Long>, CommonBoardRepository, QueryDslRecruitmentBoardRepository {
    @Query("Select sb.id From RecruitmentBoard sb " +
            "where sb.recruitmentDeadline >= CURRENT_TIMESTAMP " +
            "and sb.status = com.example.demo.application.board.dto.vo.Status.PUBLISHED " +
            "and sb.type = :boardType " +
            "order by sb.recruitmentDeadline asc, sb.id asc")
    List<Long> findPublishedId(RecruitmentBoardType boardType);

    @Query("Select Max(id) From RecruitmentBoard " +
            "where status = com.example.demo.application.board.dto.vo.Status.DRAFT " +
            "and user.id = :userId")
    Optional<Long> findFirstDraftIdByUserId(Long userId);

    @Override
    @Transactional(readOnly = true)
    default Optional<GenericBoard> doFindById(Long id) {
        Optional<RecruitmentBoard> recruitmentBoard = findById(id);
        if (recruitmentBoard.isPresent()) {
            return Optional.of(recruitmentBoard.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Query("SELECT rb FROM RecruitmentBoard rb " +
            "JOIN FETCH rb.user " +
            "WHERE rb.id = :id")
    Optional<GenericBoard> findByIdWithUser(Long id);
}
