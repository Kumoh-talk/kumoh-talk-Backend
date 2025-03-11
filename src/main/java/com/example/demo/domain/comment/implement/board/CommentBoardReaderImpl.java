package com.example.demo.domain.comment.implement.board;

import com.example.demo.domain.board.service.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentBoardReaderImpl implements GenericCommentBoardReader {
    private final BoardRepository boardRepository;

    @Override
    public boolean existsById(Long id) {
        return boardRepository.findBoardInfo(id).isPresent();
    }

    @Override
    public boolean existsByIdWithUser(Long id) {
        return boardRepository.findBoardInfo(id).isPresent();
    }

}
