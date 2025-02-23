package com.example.demo.builder;

import com.example.demo.infra.board.Repository.BoardRepository;
import com.example.demo.infra.board.Repository.LikeRepository;
import com.example.demo.domain.comment.repository.BoardCommentRepository;
import com.example.demo.domain.comment.repository.RecruitmentBoardCommentRepository;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private RecruitmentBoardRepository recruitmentBoardRepository;

    @Autowired
    private BoardCommentRepository boardCommentRepository;

    @Autowired
    private RecruitmentBoardCommentRepository recruitmentBoardCommentRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    public BoardRepository boardRepository() {
        return boardRepository;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

    public LikeRepository likeRepository() {
        return likeRepository;
    }

    public RecruitmentBoardRepository recruitmentBoardRepository() {
        return recruitmentBoardRepository;
    }

    public BoardCommentRepository boardCommentRepository() {
        return boardCommentRepository;
    }

    public RecruitmentBoardCommentRepository recruitmentBoardCommentRepository() {
        return recruitmentBoardCommentRepository;
    }

    public NotificationRepository notificationRepository() {
        return notificationRepository;
    }

}
