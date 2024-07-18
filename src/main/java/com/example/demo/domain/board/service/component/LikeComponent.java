package com.example.demo.domain.board.service.component;

import com.example.demo.domain.board.Repository.BoardRepository;
import com.example.demo.domain.board.Repository.LikeRepository;
import com.example.demo.domain.board.domain.entity.Board;
import com.example.demo.domain.board.domain.entity.Like;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.base.exception.ErrorCode;
import com.example.demo.global.base.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LikeComponent {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional //TODO : [Board] 좋아요 취소도 넣을건지 물어보기
    public void increaseLike(Long userId, Long boardId) {
        Board board = validateBoard(boardId);
        User user = validateUser(userId);
        if(likeRepository.existsByBoardIdAndUserId(boardId, userId)) {
            throw new ServiceException(ErrorCode.USER_ALREADY_LIKE_BOARD);
        }
        Like like = new Like(user, board);
        likeRepository.save(like);
    }

    private User validateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.LIKE_USER_NOT_FOUND));
        return user;
    }

    private Board validateBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ServiceException(ErrorCode.BOARD_NOT_FOUND));
        return board;
    }
}
