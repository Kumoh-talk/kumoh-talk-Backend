package com.example.demo.domain.board.Repository;

import com.example.demo.domain.board.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("SELECT EXISTS(l) FROM Like l WHERE l.board.id = :boardId AND l.user.id = :userId")
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}
