package com.example.demo.infra.builder;

import com.example.demo.domain.notification.domain.entity.Notification;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.board.category.entity.BoardCategory;
import com.example.demo.infra.board.category.entity.Category;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JpaTestFixtureBuilder {
    @Autowired
    private JpaBuilderSupporter bs;

    public Board buildBoard(Board board) {
        return bs.boardRepository().save(board);
    }

    public User buildUser(User user) {
        return bs.userRepository().save(user);
    }

    public Like buildLike(Like like) {
        return bs.likeRepository().save(like);
    }

    public RecruitmentBoard buildRecruitmentBoard(RecruitmentBoard recruitmentBoard) {
        return bs.recruitmentBoardRepository().save(recruitmentBoard);
    }

    public BoardComment buildBoardComment(BoardComment boardComment) {
        return bs.boardCommentJpaRepository().save(boardComment);
    }

    public RecruitmentBoardComment buildRecruitmentBoardComment(RecruitmentBoardComment recruitmentBoardComment) {
        return bs.recruitmentBoardCommentJpaRepository().save(recruitmentBoardComment);
    }

    public Notification buildNotification(Notification notification) {
        return bs.notificationRepository().save(notification);
    }

    public List<Category> buildCategories(List<String> categories) {
        List<Category> categoryList = categories.stream().map(Category::new).toList();
        return bs.categoryJpaRepository().saveAll(categoryList);
    }

    public void buildBoardCategories(Board publishedBOARD, List<Category> categories) {
        categories.forEach(category -> {
            bs.boardCategoryJpaRepository().save(new BoardCategory(publishedBOARD, category));
        });
    }
}
