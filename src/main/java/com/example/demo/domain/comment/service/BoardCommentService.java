package com.example.demo.domain.comment.service;

import com.example.demo.domain.board.service.entity.vo.BoardType;
import com.example.demo.domain.comment.repository.CommonCommentRepository;
import com.example.demo.domain.recruitment_board.repository.CommonBoardRepository;
import com.example.demo.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BoardCommentService extends AbstractCommentService {
    protected BoardCommentService(
            UserService userService,
            CommentNotificationService commentNotificationService,
            @Qualifier("boardCommentRepository") CommonCommentRepository<BoardType> commentRepository,
            @Qualifier("boardJpaRepository") CommonBoardRepository boardRepository) {
        super(userService, commentNotificationService, commentRepository, boardRepository);
    }
}
