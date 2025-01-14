package com.example.demo.domain.comment.domain.entity;

import com.example.demo.domain.comment.domain.dto.request.CommentRequest;
import com.example.demo.domain.recruitment_board.domain.entity.RecruitmentBoard;
import com.example.demo.domain.report.domain.Report;
import com.example.demo.domain.user.domain.User;
import com.example.demo.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recruitment_board_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE recruitment_board_comments SET deleted_at = NOW() where id = ?")
public class RecruitmentBoardComment extends BaseEntity implements Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    protected Long id;

    @Column(name = "content", nullable = false, length = 500)
    @NotBlank(message = "댓글 내용은 빈 값일 수 없습니다.")
    protected String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_board_id", nullable = false)
    private RecruitmentBoard board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private RecruitmentBoardComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<RecruitmentBoardComment> replyComments = new ArrayList<>();

    @Builder
    public RecruitmentBoardComment(String content, RecruitmentBoard recruitmentBoard, User user, RecruitmentBoardComment parentComment) {
        this.content = content;
        this.board = recruitmentBoard;
        this.user = user;
        this.parentComment = parentComment;
    }

    public static RecruitmentBoardComment fromRecruitmentBoardCommentRequest(
            User user,
            RecruitmentBoard recruitmentBoard,
            CommentRequest commentRequest,
            RecruitmentBoardComment parentComment) {

        return RecruitmentBoardComment.builder()
                .content(commentRequest.getContent())
                .recruitmentBoard(recruitmentBoard)
                .user(user)
                .parentComment(parentComment)
                .build();
    }
    
    @Override
    public List<Comment> getReplyComments() {
        return new ArrayList<>(replyComments);
    }

    @Override
    public void changeContent(String newContent) {
        this.content = newContent;
    }

    @Override
    public Report toReport(User user) {
        return Report.fromRecruitmentBoardComment(user, this);
    }
}
