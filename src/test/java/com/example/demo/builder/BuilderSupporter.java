package com.example.demo.builder;

import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.infra.board.Repository.BoardJpaRepository;
import com.example.demo.infra.board.Repository.LikeRepository;
import com.example.demo.infra.board.category.repository.CategoryJpaRepository;
import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
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
    private BoardCommentJpaRepository boardCommentJpaRepository;

    @Autowired
    private RecruitmentBoardCommentJpaRepository recruitmentBoardCommentJpaRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;


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

    public BoardCommentJpaRepository boardCommentRepository() {
        return boardCommentJpaRepository;
    }

    public RecruitmentBoardCommentJpaRepository recruitmentBoardCommentRepository() {
        return recruitmentBoardCommentJpaRepository;
    }

    public NotificationRepository notificationRepository() {
        return notificationRepository;
    }


}
