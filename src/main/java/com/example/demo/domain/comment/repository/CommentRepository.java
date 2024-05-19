package com.example.demo.domain.comment.repository;

import com.example.demo.domain.comment.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("Select distinct c from Comment c join fetch c.user " +
            "join c.board " +
            "left join fetch c.replyComments " +
            "left join fetch c.likedUsers " +
            "where c.board.id = :boardId " +
            "and c.parentComment is null " +
            "order by c.createdAt asc ")
    List<Comment> findByBoard_idOrderByCreatedAtAsc(Long boardId);
    //List<Comment> findByBoard_IdAndParentCommentOrderByCreatedAtAsc(Long boardId, Comment parentComment);


}