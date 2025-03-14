package com.example.demo.domain.comment.implement.comment;

import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.recruitment_board.domain.vo.EntireBoardType;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentBoardCommentHandler extends AbstractCommentHandler<RecruitmentBoardType> {
    public RecruitmentBoardCommentHandler(CommentRepository<RecruitmentBoardType> commentRepository) {
        super(commentRepository);
    }

    public Page<MyCommentInfo> getPageByBoardId(Long userId, Pageable pageable, EntireBoardType boardType) {
        return commentRepository.getPageByBoardId(userId, pageable, boardType.getRecruitmentBoardType());
    }
}
