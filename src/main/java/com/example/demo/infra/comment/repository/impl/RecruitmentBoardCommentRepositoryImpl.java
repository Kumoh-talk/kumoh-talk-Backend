package com.example.demo.infra.comment.repository.impl;

import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.infra.user.repository.UserJpaRepository;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RecruitmentBoardCommentRepositoryImpl extends AbstractCommentRepositoryImpl<RecruitmentBoardType> {

    public RecruitmentBoardCommentRepositoryImpl(
            RecruitmentBoardCommentJpaRepository commentJpaRepository,
            UserJpaRepository userJpaRepository,
            RecruitmentBoardRepository recruitmentBoardRepository) {
        super(commentJpaRepository, userJpaRepository, recruitmentBoardRepository, RecruitmentBoard.class, RecruitmentBoardComment.class);
    }

}
