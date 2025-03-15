package com.example.demo.domain.comment.implement.comment;

import com.example.demo.domain.comment.entity.CommentInfo;
import com.example.demo.domain.comment.entity.CommentUserInfo;
import com.example.demo.domain.comment.entity.MyCommentInfo;
import com.example.demo.domain.comment.repository.CommentRepository;
import com.example.demo.domain.recruitment_board.entity.vo.EntireBoardType;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractCommentHandler<T> {
    protected final CommentRepository<T> commentRepository;

    public Optional<CommentInfo> getById(Long id) {
        return commentRepository.getById(id);
    }

    public List<CommentInfo> getListByBoardId(Long boardId) {
        return commentRepository.getListByBoardId(boardId);
    }

    public Optional<CommentInfo> getParentComment(Long parentCommentId, Long boardId) {
        return commentRepository.getByIdAndBoardId(parentCommentId, boardId);
    }

    public abstract Page<MyCommentInfo> getPageByBoardId(Long userId, Pageable pageable, EntireBoardType entireBoardType);

    public List<CommentUserInfo> getUsersByBoardIdAndParentCommentId(Long boardId, Long parentCommentId) {
        return commentRepository.getUsersByBoardIdAndParentCommentId(boardId, parentCommentId);
    }

    public CommentInfo post(CommentInfo commentInfo) {
        return commentRepository.post(commentInfo);
    }

    public void delete(CommentInfo commentInfo) {
        commentRepository.delete(commentInfo);
    }

    public Optional<CommentInfo> patch(CommentInfo originComment, CommentInfo newComment) {
        if (newComment.getCommentUserInfo().getUserId().equals(
                originComment.getCommentUserInfo().getUserId())) {
            return commentRepository.patch(originComment, newComment);
        } else {
            throw new ServiceException(ErrorCode.ACCESS_DENIED);
        }
    }
}
