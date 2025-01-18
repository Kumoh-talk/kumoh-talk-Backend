package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.repository.CommonCommentRepository;
import com.example.demo.domain.recruitment_board.domain.vo.RecruitmentBoardType;
import com.example.demo.domain.recruitment_board.repository.CommonBoardRepository;
import com.example.demo.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentBoardCommentService extends AbstractCommentService {
    protected RecruitmentBoardCommentService(
            UserService userService,
//            CommentNotificationService commentNotificationService,
            @Qualifier("recruitmentBoardCommentRepository") CommonCommentRepository<RecruitmentBoardType> commentRepository,
            @Qualifier("recruitmentBoardRepository") CommonBoardRepository boardRepository) {
        super(userService, commentRepository, boardRepository);
    }
}
