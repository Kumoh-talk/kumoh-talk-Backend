package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.implement.board.CommentRecruitmentBoardReaderImpl;
import com.example.demo.domain.comment.implement.comment.RecruitmentBoardCommentHandler;
import com.example.demo.domain.user.implement.UserReader;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentBoardCommentService extends AbstractCommentService {
    protected RecruitmentBoardCommentService(
            UserReader userReader,
            RecruitmentBoardCommentHandler recruitmentBoardCommentHandler,
            CommentRecruitmentBoardReaderImpl commentRecruitmentBoardReader) {
        super(userReader, recruitmentBoardCommentHandler, commentRecruitmentBoardReader);
    }
}
