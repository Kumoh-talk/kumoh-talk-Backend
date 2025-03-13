package com.example.demo.infra.builder;

import com.example.demo.infra.user.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.domain.comment.repository.BoardCommentRepository;
import com.example.demo.domain.comment.repository.RecruitmentBoardCommentRepository;
import com.example.demo.domain.notification.repository.NotificationRepository;
import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import com.example.demo.infra.board.repository.BoardJpaRepository;
import com.example.demo.infra.board.repository.LikeJpaRepository;
import com.example.demo.infra.board.category.repository.BoardCategoryJpaRepository;
import com.example.demo.infra.board.category.repository.CategoryJpaRepository;

@Component
public class JpaBuilderSupporter {
	@Autowired
	private BoardJpaRepository boardJpaRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private LikeJpaRepository likeJpaRepository;

	@Autowired
	private RecruitmentBoardRepository recruitmentBoardRepository;

	@Autowired
	private BoardCommentRepository boardCommentRepository;

	@Autowired
	private RecruitmentBoardCommentRepository recruitmentBoardCommentRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private CategoryJpaRepository categoryJpaRepository;

	@Autowired
	private BoardCategoryJpaRepository boardCategoryJpaRepository;

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

	public BoardCommentRepository boardCommentRepository() {
		return boardCommentRepository;
	}

	public RecruitmentBoardCommentRepository recruitmentBoardCommentRepository() {
		return recruitmentBoardCommentRepository;
	}

	public NotificationRepository notificationRepository() {
		return notificationRepository;
	}


	public CategoryJpaRepository categoryJpaRepository() {
		return categoryJpaRepository;
	}

	public BoardCategoryJpaRepository boardCategoryJpaRepository() {
		return boardCategoryJpaRepository;
	}
}
