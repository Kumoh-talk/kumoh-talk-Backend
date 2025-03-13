package com.example.demo.builder;

import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.infra.board.category.repository.CategoryJpaRepository;
import com.example.demo.infra.board.repository.BoardJpaRepository;
import com.example.demo.infra.board.repository.LikeJpaRepository;
import com.example.demo.infra.comment.repository.jpa.BoardCommentJpaRepository;
import com.example.demo.infra.comment.repository.jpa.RecruitmentBoardCommentJpaRepository;
import com.example.demo.infra.user.repository.UserJpaRepository;
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

    public LikeJpaRepository likeRepository() {
        return likeJpaRepository;
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
