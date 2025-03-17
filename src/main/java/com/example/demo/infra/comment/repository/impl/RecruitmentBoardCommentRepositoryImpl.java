package com.example.demo.infra.comment.repository.impl;

import com.example.demo.domain.recruitment_board.entity.vo.RecruitmentBoardType;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
import com.example.demo.infra.recruitment_board.entity.RecruitmentBoard;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentBoardJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RecruitmentBoardCommentRepositoryImpl extends AbstractCommentRepositoryImpl<RecruitmentBoardType> {

    public RecruitmentBoardCommentRepositoryImpl(
            RecruitmentBoardCommentJpaRepository commentJpaRepository,
            UserJpaRepository userJpaRepository,
            RecruitmentBoardJpaRepository recruitmentBoardJpaRepository) {
        super(commentJpaRepository, userJpaRepository, recruitmentBoardJpaRepository, RecruitmentBoard.class, RecruitmentBoardComment.class);
    }

}
