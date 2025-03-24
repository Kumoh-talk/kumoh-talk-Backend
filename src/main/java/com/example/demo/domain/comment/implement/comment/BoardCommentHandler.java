package com.example.demo.domain.comment.implement.comment;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.recruitment_board.entity.vo.EntireBoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentHandler extends AbstractCommentHandler<BoardType> {
    public BoardCommentHandler(CommentRepository<BoardType> commentRepository) {
        super(commentRepository);
    }

    public Page<MyCommentInfo> getPageByBoardId(Long userId, Pageable pageable, EntireBoardType boardType) {
        return commentRepository.getPageByBoardId(userId, pageable, boardType.getBoardType());
    }
}
