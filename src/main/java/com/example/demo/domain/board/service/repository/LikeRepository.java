package com.example.demo.domain.board.service.repository;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.LikeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LikeRepository {
    LikeInfo saveLike(Long userId, Long boardId);

    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    Optional<Long> findIdByBoardIdAndUserId(Long boardId, Long userId);

    void deleteLike(Long likeId, Long userId, Long boardId);

    Page<BoardTitleInfo> findLikedBoardPageByUserId(Long userId, Pageable pageable);
}
