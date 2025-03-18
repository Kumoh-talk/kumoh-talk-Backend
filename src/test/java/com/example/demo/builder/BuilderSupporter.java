package com.example.demo.builder;

import com.example.demo.domain.user.repository.UserJpaRepository;
import com.example.demo.infra.board.category.repository.CategoryJpaRepository;
import com.example.demo.infra.board.repository.BoardJpaRepository;
import com.example.demo.infra.board.repository.LikeJpaRepository;
import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
import com.example.demo.infra.notification.repository.jpa.NotificationJpaRepository;
import com.example.demo.infra.recruitment_board.repository.jpa.RecruitmentBoardJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderSupporter {

    @Autowired
    private BoardJpaRepository boardJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private LikeJpaRepository likeJpaRepository;

    @Autowired
    private RecruitmentBoardJpaRepository recruitmentBoardJpaRepository;

    @Autowired
    private BoardCommentJpaRepository boardCommentJpaRepository;

    @Autowired
    private RecruitmentBoardCommentJpaRepository recruitmentBoardCommentJpaRepository;

    @Autowired
    private NotificationJpaRepository notificationJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;


    public BoardJpaRepository boardRepository() {
        return boardJpaRepository;
    }

    public UserJpaRepository userRepository() {
        return userJpaRepository;
    }

    public LikeJpaRepository likeRepository() {
        return likeJpaRepository;
    }

    public RecruitmentBoardJpaRepository recruitmentBoardRepository() {
        return recruitmentBoardJpaRepository;
    }

    public BoardCommentJpaRepository boardCommentRepository() {
        return boardCommentJpaRepository;
    }

    public RecruitmentBoardCommentJpaRepository recruitmentBoardCommentRepository() {
        return recruitmentBoardCommentJpaRepository;
    }

    public NotificationJpaRepository notificationRepository() {
        return notificationJpaRepository;
    }


}
