package com.example.demo.infra.recruitment_board.repository.jpa;

import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.infra.recruitment_board.entity.CommentBoard;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.repository.querydsl.QueryDslRecruitmentBoardRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RecruitmentBoardJpaRepository extends JpaRepository<RecruitmentBoard, Long>, CommentBoardJpaRepository, QueryDslRecruitmentBoardRepository {
    @Query("Select sb.id From RecruitmentBoard sb " +
            "where sb.recruitmentDeadline >= CURRENT_TIMESTAMP " +
            "and sb.status = com.example.demo.domain.board.service.entity.vo.Status.PUBLISHED " +
            "and sb.type = :boardType " +
            "order by sb.recruitmentDeadline asc, sb.id asc")
    List<Long> findPublishedId(RecruitmentBoardType boardType);


    @Override
    @Transactional(readOnly = true)
    default Optional<CommentBoard> doFindById(Long id) {
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
    Optional<CommentBoard> findByIdWithUser(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE RecruitmentBoard rb SET rb.currentMemberNum = rb.currentMemberNum + 1 WHERE rb.id = :recruitmentBoardId")
    void increaseCurrentMemberNum(Long recruitmentBoardId);

    @Transactional
    @Modifying
    @Query("UPDATE RecruitmentBoard rb SET rb.currentMemberNum = rb.currentMemberNum - 1 WHERE rb.id = :recruitmentBoardId")
    void decreaseCurrentMemberNum(Long recruitmentBoardId);

}
