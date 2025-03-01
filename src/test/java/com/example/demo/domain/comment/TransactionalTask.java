package com.example.demo.domain.comment;

import com.example.demo.builder.TestFixtureBuilder;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.user.domain.User;
import com.example.demo.infra.board.entity.Board;
import com.example.demo.infra.board.entity.Like;
import com.example.demo.infra.comment.entity.BoardComment;
import com.example.demo.infra.comment.entity.RecruitmentBoardComment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.example.demo.fixture.comment.CommentFixtures.BOARD_COMMENT;
import static com.example.demo.fixture.comment.CommentFixtures.RECRUITMENT_BOARD_COMMENT;
import static com.example.demo.fixture.recruitment_board.RecruitmentBoardFixtures.PUBLISHED_STUDY_RECRUITMENT_BOARD;
import static com.example.demo.infra.fixture.board.BoardFixtures.PUBLISHED_SEMINAR_BOARD;
import static com.example.demo.infra.fixture.board.LikeFixtures.LIKE;

@Component
@Transactional
public class TransactionalTask {
    @Autowired
    private TestFixtureBuilder testFixtureBuilder;

    @PersistenceContext
    private EntityManager em;

    public BoardComment createBoardAndComment(User boardWriteUser, User commentWriteUser) {
        User savedBoardWriteUser = testFixtureBuilder.buildUser(boardWriteUser);
        User savedCommentWriteUser = testFixtureBuilder.buildUser(commentWriteUser);
        Board savedBoard = testFixtureBuilder.buildBoard(PUBLISHED_SEMINAR_BOARD(savedBoardWriteUser));
        return testFixtureBuilder.buildBoardComment(BOARD_COMMENT(savedBoard, savedCommentWriteUser, null));
    }

    public Like createBoardAndLike(User boardWriteUser, User likeUser) {
        User savedBoardWriteUser = testFixtureBuilder.buildUser(boardWriteUser);
        User savedLikeUser = testFixtureBuilder.buildUser(likeUser);
        Board savedBoard = testFixtureBuilder.buildBoard(PUBLISHED_SEMINAR_BOARD(savedBoardWriteUser));
        return testFixtureBuilder.buildLike(LIKE(savedLikeUser, savedBoard));
    }

    public RecruitmentBoardComment createRecruitmentBoardAndComment(User recruitmentBoardWriteUser, User commentWriteUser) {
        User savedRecruitmentBoardWriteUser = testFixtureBuilder.buildUser(recruitmentBoardWriteUser);
        User savedCommentWriteUser = testFixtureBuilder.buildUser(commentWriteUser);
        RecruitmentBoard savedRecruitmentBoard = testFixtureBuilder.buildRecruitmentBoard(PUBLISHED_STUDY_RECRUITMENT_BOARD(savedRecruitmentBoardWriteUser));

        return testFixtureBuilder.buildRecruitmentBoardComment(RECRUITMENT_BOARD_COMMENT(savedRecruitmentBoard, savedCommentWriteUser, null));
    }

    public void cleanUp() {
        try {
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0;").executeUpdate();

            Set<EntityType<?>> entities = em.getMetamodel().getEntities();
            for (EntityType<?> entity : entities) {
                String entityName = entity.getName();
                em.createQuery("DELETE FROM " + entityName).executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Failed to clean up database: " + e.getMessage());
        } finally {
            em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1;").executeUpdate();
        }
    }
}
