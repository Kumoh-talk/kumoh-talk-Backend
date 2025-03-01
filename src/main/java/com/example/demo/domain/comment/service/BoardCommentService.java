package com.example.demo.domain.comment.service;

import com.example.demo.domain.comment.implement.board.CommentBoardReaderImpl;
import com.example.demo.domain.comment.implement.comment.BoardCommentHandler;
import com.example.demo.domain.user.implement.UserReader;
import org.springframework.stereotype.Service;

@Service
public class BoardCommentService extends AbstractCommentService {
    protected BoardCommentService(
            UserReader userReader,
            BoardCommentHandler boardCommentHandler,
            CommentBoardReaderImpl commentBoardReader) {
        super(userReader, boardCommentHandler, commentBoardReader);
    }
}
