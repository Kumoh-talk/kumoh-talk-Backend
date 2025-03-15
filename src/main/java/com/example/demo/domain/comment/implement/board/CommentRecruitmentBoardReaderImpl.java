package com.example.demo.domain.comment.implement.board;

import com.example.demo.domain.recruitment_board.repository.RecruitmentBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentRecruitmentBoardReaderImpl implements GenericCommentBoardReader {
    private final RecruitmentBoardRepository recruitmentBoardRepository;

    @Override
    public boolean existsById(Long id) {
        return recruitmentBoardRepository.getById(id).isPresent();
    }

    @Override
    public boolean existsByIdWithUser(Long id) {
        return recruitmentBoardRepository.getByIdWithUser(id).isPresent();
    }
}
