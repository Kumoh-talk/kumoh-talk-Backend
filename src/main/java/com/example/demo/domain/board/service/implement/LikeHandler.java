package com.example.demo.domain.board.service.implement;

import com.example.demo.domain.board.service.entity.BoardTitleInfo;
import com.example.demo.domain.board.service.entity.LikeInfo;
import com.example.demo.domain.board.service.repository.LikeRepository;
import com.example.demo.global.base.dto.page.GlobalPageResponse;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LikeHandler {
    private final LikeRepository likeRepository;

    @Transactional
    public LikeInfo increaseLike(Long userId, Long boardId) {
        return likeRepository.saveLike(userId, boardId);
    }


    @Transactional(readOnly = true)
    public GlobalPageResponse<BoardTitleInfo> getLikes(Long userId, Pageable pageable) {
        return GlobalPageResponse.create(likeRepository.findLikedBoardPageByUserId(userId, pageable));
    }


    @Transactional
    public void decreaseLike(Long userId, Long boardId) {
        likeRepository.deleteLike(userId, boardId);
    }

    public void validateExistLike(Long boardId, Long userId) {
        if (likeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new ServiceException(ErrorCode.USER_ALREADY_LIKE_BOARD);
        }
    }

    public Optional<Long> findLikeId(Long boardId, Long userId) {
        return likeRepository.findIdByBoardIdAndUserId(boardId, userId);
    }

    public List<Long> removeAllByBoardId(Long boardId) {
        return likeRepository.deleteLikesByBoardId(boardId);
    }
}
