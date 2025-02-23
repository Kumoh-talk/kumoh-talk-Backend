package com.example.demo.builder;

import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.infra.board.Repository.LikeRepository;
import com.example.demo.domain.comment.repository.BoardCommentRepository;
import com.example.demo.domain.comment.repository.RecruitmentBoardCommentRepository;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.domain.user.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    @Autowired
    private BoardJpaRepository boardJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

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


    public BoardJpaRepository boardRepository() {
        return boardJpaRepository;
    }

    public UserJpaRepository userRepository() {
        return userJpaRepository;
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
